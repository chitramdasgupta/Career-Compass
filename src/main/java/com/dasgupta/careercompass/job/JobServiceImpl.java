package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.bookmark.BookmarkService;
import com.dasgupta.careercompass.exception.ResourceNotFoundException;
import com.dasgupta.careercompass.jobApplication.JobApplication;
import com.dasgupta.careercompass.jobApplication.JobApplicationDto;
import com.dasgupta.careercompass.jobApplication.JobApplicationMapper;
import com.dasgupta.careercompass.jobApplication.JobApplicationRepository;
import com.dasgupta.careercompass.questionnaire.Questionnaire;
import com.dasgupta.careercompass.questionnaire.QuestionnaireDto;
import com.dasgupta.careercompass.questionnaire.QuestionnaireMapper;
import com.dasgupta.careercompass.questionnaire.QuestionnaireRepository;
import com.dasgupta.careercompass.questionnaire.question.Question;
import com.dasgupta.careercompass.questionnaire.question.QuestionMapper;
import com.dasgupta.careercompass.questionnaire.question.QuestionRepository;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestion;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionDto;
import com.dasgupta.careercompass.user.Role;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobServiceImpl implements JobService {
    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final BookmarkService bookmarkService;
    private final JobCreateMapper jobCreateMapper;
    private final LoggedInCompanyJobMapper loggedInCompanyJobMapper;
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final QuestionnaireMapper questionnaireMapper;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationMapper jobApplicationMapper;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository, JobMapper jobMapper, BookmarkService bookmarkService,
                          JobCreateMapper jobCreateMapper, LoggedInCompanyJobMapper loggedInCompanyJobMapper,
                          QuestionnaireRepository questionnaireRepository, QuestionnaireMapper questionnaireMapper,
                          QuestionRepository questionRepository, QuestionMapper questionMapper,
                          JobApplicationRepository jobApplicationRepository, JobApplicationMapper jobApplicationMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.bookmarkService = bookmarkService;
        this.jobCreateMapper = jobCreateMapper;
        this.loggedInCompanyJobMapper = loggedInCompanyJobMapper;
        this.questionnaireRepository = questionnaireRepository;
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.questionnaireMapper = questionnaireMapper;
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobApplicationMapper = jobApplicationMapper;
    }

    @Override
    public Page<JobDto> getAllJobs(Pageable pageable, Integer userId, Role role) {
        Page<Job> jobPage = jobRepository.findAll(pageable);
        log.info("jobMapper for all jobs about to be called");

        return jobPage.map(job -> {
            JobDto jobDto = jobMapper.toDto(job);
            if (role == Role.ROLE_CANDIDATE) {
                jobDto.setBookmarked(bookmarkService.isJobBookmarked(userId, job.getId()));
            }

            return jobDto;
        });
    }

    @Override
    public Optional<JobDto> getJobById(int id, Integer userId, Role role) {
        log.info("Service getJobById called with id={}", id);

        Job job = jobRepository.findById(id).orElseThrow();
        log.info("The found job has the id: {}", job.getId());

        log.info("The job mapper call to convert the job to DTO is about to be called");
        JobDto dto = jobMapper.toDto(job);
        log.info("The dto has the id: {}", dto.getId());
        if (role == Role.ROLE_CANDIDATE) {
            dto.setBookmarked(bookmarkService.isJobBookmarked(userId, id));
        }

        return jobRepository.findById(id).map(jobMapper::toDto);
    }

    @Override
    public Page<JobDto> getJobsByCompany(Pageable pageable, Integer companyId) {
        Page<Job> jobPage = jobRepository.findByCompanyId(companyId, pageable);

        return jobPage.map(jobMapper::toDto);
    }

    @Override
    public Page<LoggedInCompanyJobDto> getLoggedInCompanyJobs(Pageable pageable, Integer companyId) {
        Page<Job> jobPage = jobRepository.findByCompanyId(companyId, pageable);

        return jobPage.map(loggedInCompanyJobMapper::toDto);
    }

    @Override
    public JobDto createJob(JobCreateRequestDto jobCreateRequestDto, Integer companyId) {
        Job job = jobCreateMapper.toEntity(jobCreateRequestDto);
        job.setStatus(JobStatus.QUESTIONNAIRE_PENDING);

        job = jobRepository.save(job);

        return jobMapper.toDto(job);
    }

    @Override
    public JobDto createQuestionnaireForJob(int jobId, QuestionnaireDto questionnaireDto, Integer userId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCompany().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (job.getStatus() != JobStatus.QUESTIONNAIRE_PENDING) {
            throw new RuntimeException("Job is not in Questionnaire Pending status");
        }

        // Create the questionnaire
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDescription(questionnaireDto.getDescription());
        questionnaire.setJob(job);

        List<QuestionnaireQuestion> questionnaireQuestions = new ArrayList<>();

        // Save all Question entities first
        for (QuestionnaireQuestionDto qqDto : questionnaireDto.getQuestionnaireQuestions()) {
            Question question = questionMapper.toEntity(qqDto.getQuestion());
            question = questionRepository.save(question);  // Save each Question

            QuestionnaireQuestion qq = new QuestionnaireQuestion();
            qq.setQuestion(question);
            qq.setDisplayOrder(qqDto.getDisplayOrder());
            qq.setQuestionnaire(questionnaire);
            questionnaireQuestions.add(qq);
        }

        Set<QuestionnaireQuestion> questionnaireQuestionsSet = Set.copyOf(questionnaireQuestions);
        questionnaire.setQuestionnaireQuestions(questionnaireQuestionsSet);

        questionnaireRepository.save(questionnaire);

        // Update job status
        job.setStatus(JobStatus.READY_TO_POST);
        job = jobRepository.save(job);

        return jobMapper.toDto(job);
    }

    @Override
    public JobDto postJob(int jobId, Integer userId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCompany().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (job.getStatus() != JobStatus.READY_TO_POST) {
            throw new RuntimeException("Job is not ready to post");
        }

        job.setStatus(JobStatus.POSTED);
        job = jobRepository.save(job);

        return jobMapper.toDto(job);
    }

    @Override
    public Optional<QuestionnaireDto> getJobQuestionnaire(int jobId, Integer userId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCompany().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return Optional.of(questionnaireMapper.toDto(job.getQuestionnaire()));
    }

    @Override
    public Page<JobApplicationDto> getJobApplications(Pageable pageable, int jobId, int userId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!Objects.equals(job.getCompany().getUser().getId(), userId)) {
            throw new AccessDeniedException("You don't have permission to view applications for this job");
        }

        Page<JobApplication> applications = jobApplicationRepository.findByJobId(jobId, pageable);
        return applications.map(jobApplicationMapper::toDto);
    }

}