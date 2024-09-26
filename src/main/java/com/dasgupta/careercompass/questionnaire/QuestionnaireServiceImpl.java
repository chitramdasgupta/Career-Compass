package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.job.*;
import com.dasgupta.careercompass.questionnaire.question.Question;
import com.dasgupta.careercompass.questionnaire.question.QuestionMapper;
import com.dasgupta.careercompass.questionnaire.question.QuestionRepository;
import com.dasgupta.careercompass.questionnaire.question.QuestionService;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestion;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionDto;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionMapper;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionnaireQuestionMapper questionnaireQuestionMapper;
    private final JobService jobService;
    private final JobMapper jobMapper;
    private final QuestionService questionService;

    public QuestionnaireServiceImpl(QuestionnaireRepository questionnaireRepository,
                                    QuestionnaireQuestionMapper questionnaireQuestionMapper, JobService jobService,
                                    JobMapper jobMapper, QuestionService questionService) {
        this.questionnaireRepository = questionnaireRepository;
        this.questionnaireQuestionMapper = questionnaireQuestionMapper;
        this.jobService = jobService;
        this.jobMapper = jobMapper;
        this.questionService = questionService;
    }

    @Override
    public List<QuestionnaireQuestionDto> getQuestionsByQuestionnaireId(Integer id) {
        return questionnaireRepository.findById(id).map(Questionnaire::getQuestionnaireQuestions)
                .map(questions -> questions.stream().map(questionnaireQuestionMapper::toDto)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("Questionnaire not found"));
    }

    @Override
    public JobDto createQuestionnaireForJob(int jobId, QuestionnaireDto questionnaireDto, Integer userId) {
        JobDto job = jobService.getJobById(jobId, userId);

        if (!job.getCompany().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Unauthorized access");
        }

        if (job.getStatus() != JobStatus.QUESTIONNAIRE_PENDING) {
            throw new ValidationException("Job is not in Questionnaire Pending status");
        }

        createAndSaveQuestionnaire(questionnaireDto, jobMapper.toEntity(job));

        return job;
    }

    @Override
    public QuestionnaireDto getJobQuestionnaire(int jobId, Integer userId) {
        JobDto job = jobService.getJobById(jobId, userId);

        if (!job.getCompany().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Unauthorized access");
        }

        return job.getQuestionnaire();
    }

    private void createAndSaveQuestionnaire(QuestionnaireDto questionnaireDto, Job job) {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDescription(questionnaireDto.getDescription());
        questionnaire.setJob(job);

        Set<QuestionnaireQuestion> questionnaireQuestions = questionnaireDto.getQuestionnaireQuestions().stream()
                .map(qqDto -> createQuestionnaireQuestion(qqDto, questionnaire))
                .collect(Collectors.toSet());

        questionnaire.setQuestionnaireQuestions(questionnaireQuestions);
    }

    private QuestionnaireQuestion createQuestionnaireQuestion(QuestionnaireQuestionDto qqDto, Questionnaire questionnaire) {
        Question question = questionService.createQuestion(qqDto.getQuestion());

        QuestionnaireQuestion qq = new QuestionnaireQuestion();
        qq.setQuestion(question);
        qq.setDisplayOrder(qqDto.getDisplayOrder());
        qq.setQuestionnaire(questionnaire);

        return qq;
    }
}