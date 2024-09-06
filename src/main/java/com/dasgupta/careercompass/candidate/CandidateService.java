package com.dasgupta.careercompass.candidate;

import java.util.Optional;

public interface CandidateService {
    Optional<CandidateDto> getCandidateByUserId(Integer userId);
}
