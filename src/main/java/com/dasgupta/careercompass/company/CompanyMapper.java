package com.dasgupta.careercompass.company;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyDto toDto(Company company);

    Company toEntity(CompanyDto companyDto);
}