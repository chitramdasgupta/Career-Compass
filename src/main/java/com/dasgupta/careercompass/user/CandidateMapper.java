package com.dasgupta.careercompass.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CandidateMapper {
    @Mapping(source = "user", target = "user")
    @Mapping(target = "degree", ignore = true)
    @Mapping(target = "department", ignore = true)
    CandidateDto toDto(Candidate candidate);

    @Mapping(source = "user", target = "user")
    @Mapping(target = "jobApplications", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "bookmarks", ignore = true)
    Candidate toEntity(CandidateDto candidateDto);
}
