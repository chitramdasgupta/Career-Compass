package com.dasgupta.careercompass.questionnaire.question;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionMapper questionMapper;

    private QuestionServiceImpl questionService;

    @BeforeEach
    void setUp() {
        questionService = new QuestionServiceImpl(questionRepository, questionMapper);
    }

    @Test
    void getQuestionById_WhenQuestionExists_ShouldReturnQuestionDto() {
        // Arrange
        int questionId = 1;
        Question question = new Question();
        question.setId(questionId);
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(questionId);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(questionMapper.toDto(question)).thenReturn(questionDto);

        // Act
        QuestionDto result = questionService.getQuestionById(questionId);

        // Assert
        assertNotNull(result);
        assertEquals(questionId, result.getId());
        verify(questionRepository).findById(questionId);
        verify(questionMapper).toDto(question);
    }

    @Test
    void getQuestionById_WhenQuestionDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        int questionId = 1;
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> questionService.getQuestionById(questionId));
        verify(questionRepository).findById(questionId);
        verify(questionMapper, never()).toDto(any(Question.class));
    }

    @Test
    void createQuestion_ShouldSaveAndReturnQuestion() {
        // Arrange
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(1);
        questionDto.setText("Sample question?");

        Question question = new Question();
        question.setId(1);
        question.setText("Sample question?");

        when(questionMapper.toEntity(questionDto)).thenReturn(question);
        when(questionRepository.save(question)).thenReturn(question);

        // Act
        Question result = questionService.createQuestion(questionDto);

        // Assert
        assertNotNull(result);
        assertEquals(questionDto.getId(), result.getId());
        assertEquals(questionDto.getText(), result.getText());
        verify(questionMapper).toEntity(questionDto);
        verify(questionRepository).save(question);
    }
}
