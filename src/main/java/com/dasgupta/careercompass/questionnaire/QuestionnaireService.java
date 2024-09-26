package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionDto;

import java.util.List;
import java.util.Optional;

public interface QuestionnaireService {
    List<QuestionnaireQuestionDto> getQuestionsByQuestionnaireId(Integer id);

    JobDto createQuestionnaireForJob(int jobId, QuestionnaireDto questionnaireDto, Integer userId);

    QuestionnaireDto getJobQuestionnaire(int jobId, Integer id);
}
