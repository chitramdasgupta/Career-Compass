package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.job.JobMapper;
import com.dasgupta.careercompass.user.CandidateMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {JobMapper.class, CandidateMapper.class})
public interface JobApplicationMapper {
    JobApplicationDto toDto(JobApplication jobApplication);

    JobApplication toEntity(JobApplicationDto jobApplicationDto);
}
