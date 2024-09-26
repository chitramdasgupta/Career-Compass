package com.dasgupta.careercompass.questionnaire.question;

public interface QuestionService {
    QuestionDto getQuestionById(int id);

    Question createQuestion(QuestionDto questionDto);
}
