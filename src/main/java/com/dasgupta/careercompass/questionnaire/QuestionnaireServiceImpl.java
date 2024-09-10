package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestion;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionDto;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionnaireQuestionMapper questionnaireQuestionMapper;

    @Autowired
    public QuestionnaireServiceImpl(QuestionnaireRepository questionnaireRepository, QuestionnaireQuestionMapper questionnaireQuestionMapper) {
        this.questionnaireRepository = questionnaireRepository;
        this.questionnaireQuestionMapper = questionnaireQuestionMapper;
    }

    @Override
    public List<QuestionnaireQuestionDto> getQuestionsByQuestionnaireId(Integer id) {
        return questionnaireRepository.findById(id).map(Questionnaire::getQuestionnaireQuestions).map(questions -> questions.stream().map(questionnaireQuestionMapper::toDto).collect(Collectors.toList())).orElseThrow(() -> new RuntimeException("Questionnaire not found"));
    }

    @Override
    public Questionnaire createQuestionnaire(QuestionnaireDto questionnaireDto) {
        Questionnaire questionnaire = new Questionnaire();
        List<QuestionnaireQuestion> questions = questionnaireDto.getQuestionnaireQuestions().stream().map(questionnaireQuestionMapper::toEntity).collect(Collectors.toList());

        questionnaire.setQuestionnaireQuestions((Set<QuestionnaireQuestion>) questions);
        return questionnaireRepository.save(questionnaire);
    }
}