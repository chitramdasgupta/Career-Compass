package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionnaireControllerTest {

    @Mock
    private QuestionnaireService questionnaireService;

    private QuestionnaireController questionnaireController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        questionnaireController = new QuestionnaireController(questionnaireService);
    }

    @Test
    void getQuestionsByQuestionnaireId_WhenQuestionsExist_ShouldReturnQuestions() {
        Integer questionnaireId = 1;
        List<QuestionnaireQuestionDto> expectedQuestions = Arrays.asList(
                new QuestionnaireQuestionDto(),
                new QuestionnaireQuestionDto()
        );

        when(questionnaireService.getQuestionsByQuestionnaireId(questionnaireId)).thenReturn(expectedQuestions);

        ResponseEntity<List<QuestionnaireQuestionDto>> response = questionnaireController.getQuestionsByQuestionnaireId(questionnaireId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedQuestions, response.getBody());
        verify(questionnaireService).getQuestionsByQuestionnaireId(questionnaireId);
    }

    @Test
    void getQuestionsByQuestionnaireId_WhenNoQuestionsExist_ShouldReturnEmptyList() {
        Integer questionnaireId = 1;
        List<QuestionnaireQuestionDto> emptyList = Collections.emptyList();

        when(questionnaireService.getQuestionsByQuestionnaireId(questionnaireId)).thenReturn(emptyList);

        ResponseEntity<List<QuestionnaireQuestionDto>> response = questionnaireController.getQuestionsByQuestionnaireId(questionnaireId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyList, response.getBody());
        verify(questionnaireService).getQuestionsByQuestionnaireId(questionnaireId);
    }

    @Test
    void getQuestionsByQuestionnaireId_WhenServiceThrowsException_ShouldPropagateException() {
        Integer questionnaireId = 1;
        RuntimeException expectedException = new RuntimeException("Service error");

        when(questionnaireService.getQuestionsByQuestionnaireId(questionnaireId)).thenThrow(expectedException);

        assertThrows(RuntimeException.class, () -> questionnaireController.getQuestionsByQuestionnaireId(questionnaireId));
        verify(questionnaireService).getQuestionsByQuestionnaireId(questionnaireId);
    }
}
