package com.dasgupta.careercompass.questionnaire;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Integer> {
    Questionnaire getQuestionnaireByJobId(int jobId);
}