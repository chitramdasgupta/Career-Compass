package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("questionnaires/")
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;

    @Autowired
    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @GetMapping("{id}/questions")
    public ResponseEntity<List<QuestionnaireQuestionDto>> getQuestionsByQuestionnaireId(@PathVariable Long id) {
        List<QuestionnaireQuestionDto> questions = questionnaireService.getQuestionsByQuestionnaireId(id);
        return ResponseEntity.ok(questions);
    }
}
