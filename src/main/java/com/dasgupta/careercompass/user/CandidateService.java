package com.dasgupta.careercompass.user;

import java.util.Optional;

public interface CandidateService {
    Optional<CandidateDto> getCandidateByUserId(Integer userId);
}
