package com.dasgupta.careercompass.questionnaire.answer;

import com.dasgupta.careercompass.candidate.Candidate;
import com.dasgupta.careercompass.candidate.CandidateDto;
import com.dasgupta.careercompass.candidate.CandidateMapper;
import com.dasgupta.careercompass.jobapplication.JobApplication;
import com.dasgupta.careercompass.questionnaire.question.Question;
import com.dasgupta.careercompass.questionnaire.question.QuestionDto;
import com.dasgupta.careercompass.questionnaire.question.QuestionMapper;
import com.dasgupta.careercompass.questionnaire.question.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionService questionService;

    @Mock
    private QuestionMapper questionMapper;

    @Mock
    private CandidateMapper candidateMapper;

    private AnswerServiceImpl answerService;

    @BeforeEach
    void setUp() {
        answerService = new AnswerServiceImpl(answerRepository, questionService, questionMapper, candidateMapper);
    }

    @Test
    void createAnswersForJobApplication_ShouldCreateAndSaveAnswers() {
        // Arrange
        JobApplication jobApplication = new JobApplication();
        jobApplication.setId(1);

        CandidateDto candidateDto = new CandidateDto();
        candidateDto.setId(1);

        Candidate candidate = new Candidate();
        candidate.setId(1);

        Map<Integer, String> responses = new HashMap<>();
        responses.put(1, "Answer 1");
        responses.put(2, "Answer 2");

        QuestionDto questionDto1 = new QuestionDto();
        questionDto1.setId(1);
        QuestionDto questionDto2 = new QuestionDto();
        questionDto2.setId(2);

        Question question1 = new Question();
        question1.setId(1);
        Question question2 = new Question();
        question2.setId(2);

        when(questionService.getQuestionById(1)).thenReturn(questionDto1);
        when(questionService.getQuestionById(2)).thenReturn(questionDto2);
        when(questionMapper.toEntity(questionDto1)).thenReturn(question1);
        when(questionMapper.toEntity(questionDto2)).thenReturn(question2);
        when(candidateMapper.toEntity(candidateDto)).thenReturn(candidate);

        // Act
        answerService.createAnswersForJobApplication(jobApplication, responses, candidateDto);

        // Assert
        verify(questionService, times(2)).getQuestionById(anyInt());
        verify(questionMapper, times(2)).toEntity(any(QuestionDto.class));
        verify(candidateMapper, times(2)).toEntity(candidateDto);
        verify(answerRepository, times(2)).save(argThat(answer ->
                (answer.getQuestion().getId() == 1 && answer.getResponse().equals("Answer 1")) ||
                        (answer.getQuestion().getId() == 2 && answer.getResponse().equals("Answer 2"))
        ));
    }

    @Test
    void createAnswersForJobApplication_WithEmptyResponses_ShouldNotSaveAnswers() {
        // Arrange
        JobApplication jobApplication = new JobApplication();
        CandidateDto candidateDto = new CandidateDto();
        Map<Integer, String> responses = new HashMap<>();

        // Act
        answerService.createAnswersForJobApplication(jobApplication, responses, candidateDto);

        // Assert
        verifyNoInteractions(questionService, questionMapper, candidateMapper, answerRepository);
    }
}
