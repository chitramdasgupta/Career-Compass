package com.dasgupta.careercompass.questionnaire;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireQuestionRepository extends JpaRepository<QuestionnaireQuestion, Long> {
}