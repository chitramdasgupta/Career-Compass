package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.company.CompanyMapper;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface JobMapper {

    JobDto toDto(Job job);

    @InheritInverseConfiguration
    Job toEntity(JobDto jobDto);
}