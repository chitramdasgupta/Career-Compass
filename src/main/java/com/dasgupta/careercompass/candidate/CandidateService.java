package com.dasgupta.careercompass.candidate;

import com.dasgupta.careercompass.user.User;

public interface CandidateService {
    CandidateDto getCandidateByUserId(Integer userId);

    CandidateDto createCandidate(User user);
}
