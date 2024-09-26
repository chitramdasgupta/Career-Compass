package com.dasgupta.careercompass.questionnaire.answer;

import com.dasgupta.careercompass.candidate.CandidateDto;
import com.dasgupta.careercompass.jobApplication.JobApplication;

import java.util.Map;

public interface AnswerService {
    void createAnswersForJobApplication(JobApplication jobApplication, Map<Integer, String> responses, CandidateDto candidate);
}
