package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.job.*;
import com.dasgupta.careercompass.questionnaire.question.Question;
import com.dasgupta.careercompass.questionnaire.question.QuestionDto;
import com.dasgupta.careercompass.questionnaire.question.QuestionService;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestion;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionDto;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionMapper;
import com.dasgupta.careercompass.user.UserDto;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionnaireServiceImplTest {

    @Mock
    private QuestionnaireRepository questionnaireRepository;
    @Mock
    private QuestionnaireQuestionMapper questionnaireQuestionMapper;
    @Mock
    private JobService jobService;
    @Mock
    private JobMapper jobMapper;
    @Mock
    private QuestionService questionService;
    @Mock
    private QuestionnaireMapper questionnaireMapper;

    private QuestionnaireServiceImpl questionnaireService;

    @BeforeEach
    void setUp() {
        questionnaireService = new QuestionnaireServiceImpl(
                questionnaireRepository, questionnaireQuestionMapper, jobService,
                jobMapper, questionService, questionnaireMapper);
    }

    @Test
    void getQuestionsByQuestionnaireId_ShouldReturnQuestions() {
        // Arrange
        Integer questionnaireId = 1;
        Questionnaire questionnaire = new Questionnaire();
        QuestionnaireQuestion qq1 = new QuestionnaireQuestion();
        QuestionnaireQuestion qq2 = new QuestionnaireQuestion();
        questionnaire.setQuestionnaireQuestions(new HashSet<>(Arrays.asList(qq1, qq2)));

        QuestionnaireQuestionDto qqDto1 = new QuestionnaireQuestionDto();
        QuestionnaireQuestionDto qqDto2 = new QuestionnaireQuestionDto();

        when(questionnaireRepository.findById(questionnaireId)).thenReturn(Optional.of(questionnaire));
        when(questionnaireQuestionMapper.toDto(qq1)).thenReturn(qqDto1);
        when(questionnaireQuestionMapper.toDto(qq2)).thenReturn(qqDto2);

        // Act
        List<QuestionnaireQuestionDto> result = questionnaireService.getQuestionsByQuestionnaireId(questionnaireId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(qqDto1));
        assertTrue(result.contains(qqDto2));
        verify(questionnaireRepository).findById(questionnaireId);
        verify(questionnaireQuestionMapper, times(2)).toDto(any(QuestionnaireQuestion.class));
    }

    @Test
    void getQuestionsByQuestionnaireId_WhenQuestionnaireNotFound_ShouldThrowException() {
        // Arrange
        Integer questionnaireId = 1;
        when(questionnaireRepository.findById(questionnaireId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> questionnaireService.getQuestionsByQuestionnaireId(questionnaireId));
        verify(questionnaireRepository).findById(questionnaireId);
    }

    @Test
    void createQuestionnaireForJob_WhenAuthorized_ShouldCreateQuestionnaire() {
        // Arrange
        int jobId = 1;
        int userId = 1;
        QuestionnaireDto questionnaireDto = new QuestionnaireDto();
        questionnaireDto.setDescription("Test Questionnaire");
        QuestionnaireQuestionDto qqDto = new QuestionnaireQuestionDto();
        qqDto.setQuestion(new QuestionDto());
        questionnaireDto.setQuestionnaireQuestions(Collections.singleton(qqDto));

        JobDto jobDto = new JobDto();
        CompanyDto companyDto = new CompanyDto();
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        companyDto.setUser(userDto);
        jobDto.setCompany(companyDto);
        jobDto.setStatus(JobStatus.QUESTIONNAIRE_PENDING);

        Job job = new Job();

        when(jobService.getJobById(jobId, userId)).thenReturn(jobDto);
        when(jobMapper.toEntity(jobDto)).thenReturn(job);
        when(questionService.createQuestion(any())).thenReturn(new Question());

        // Act
        JobDto result = questionnaireService.createQuestionnaireForJob(jobId, questionnaireDto, userId);

        // Assert
        assertNotNull(result);
        verify(jobService).getJobById(jobId, userId);
        verify(jobMapper).toEntity(jobDto);
        verify(questionService).createQuestion(any());
    }

    @Test
    void createQuestionnaireForJob_WhenUnauthorized_ShouldThrowAccessDeniedException() {
        // Arrange
        int jobId = 1;
        int userId = 1;
        int unauthorizedUserId = 2;
        QuestionnaireDto questionnaireDto = new QuestionnaireDto();

        JobDto jobDto = new JobDto();
        CompanyDto companyDto = new CompanyDto();
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        companyDto.setUser(userDto);
        jobDto.setCompany(companyDto);

        when(jobService.getJobById(jobId, unauthorizedUserId)).thenReturn(jobDto);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> questionnaireService.createQuestionnaireForJob(jobId, questionnaireDto, unauthorizedUserId));
        verify(jobService).getJobById(jobId, unauthorizedUserId);
    }

    @Test
    void createQuestionnaireForJob_WhenJobNotInCorrectStatus_ShouldThrowValidationException() {
        // Arrange
        int jobId = 1;
        int userId = 1;
        QuestionnaireDto questionnaireDto = new QuestionnaireDto();

        JobDto jobDto = new JobDto();
        CompanyDto companyDto = new CompanyDto();
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        companyDto.setUser(userDto);
        jobDto.setCompany(companyDto);
        jobDto.setStatus(JobStatus.POSTED);

        when(jobService.getJobById(jobId, userId)).thenReturn(jobDto);

        // Act & Assert
        assertThrows(ValidationException.class, () -> questionnaireService.createQuestionnaireForJob(jobId, questionnaireDto, userId));
        verify(jobService).getJobById(jobId, userId);
    }

    @Test
    void getJobQuestionnaire_ShouldReturnQuestionnaire() {
        // Arrange
        int jobId = 1;
        int userId = 1;
        Questionnaire questionnaire = new Questionnaire();
        QuestionnaireDto questionnaireDto = new QuestionnaireDto();

        when(questionnaireRepository.getQuestionnaireByJobId(jobId)).thenReturn(questionnaire);
        when(questionnaireMapper.toDto(questionnaire)).thenReturn(questionnaireDto);

        // Act
        QuestionnaireDto result = questionnaireService.getJobQuestionnaire(jobId, userId);

        // Assert
        assertNotNull(result);
        verify(questionnaireRepository).getQuestionnaireByJobId(jobId);
        verify(questionnaireMapper).toDto(questionnaire);
    }
}
