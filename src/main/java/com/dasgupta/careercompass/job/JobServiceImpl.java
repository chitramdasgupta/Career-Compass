package com.dasgupta.careercompass.job;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Page<JobDto> getAllJobs(Pageable pageable) {
        Page<Job> jobPage = jobRepository.findAll(pageable);
        return jobPage.map(this::convertToDto);
    }

    @Override
    public Optional<JobDto> getJobById(int id) {
        return jobRepository.findById(id).map(this::convertToDto);
    }

    private JobDto convertToDto(Job job) {
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setMinimumRequirement(job.getMinimumRequirement());
        dto.setDesiredRequirement(job.getDesiredRequirement());
        dto.setCity(job.getCity());
        dto.setCountry(job.getCountry() != null ? job.getCountry().getName() : null);
        dto.setJobLocation(job.getJobLocation() != null ? job.getJobLocation().name() : null);
        dto.setCurrency(job.getCurrency() != null ? job.getCurrency().getName() : null);
        dto.setMinimumSalary(job.getMinimumSalary());
        dto.setMaximumSalary(job.getMaximumSalary());

        // Set the questionnaire ID
        dto.setQuestionnaireId(job.getQuestionnaire() != null ? job.getQuestionnaire().getId() : null);

        return dto;
    }
}
