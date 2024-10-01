package com.dasgupta.careercompass.user;

import com.dasgupta.careercompass.candidate.CandidateDto;
import com.dasgupta.careercompass.candidate.CandidateService;
import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.company.CompanyService;
import com.dasgupta.careercompass.job.JobService;
import com.dasgupta.careercompass.job.LoggedInCompanyJobDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private CandidateService candidateService;
    @Mock
    private CompanyService companyService;
    @Mock
    private JobService jobService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, candidateService, companyService, jobService);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getCurrentUser_WhenRoleIsCandidate_ShouldReturnCandidateDto() {
        User user = new User().setId(1).setEmail("test@example.com").setRole(Role.ROLE_CANDIDATE);
        UserDto userDto = new UserDto().setId(1).setEmail("test@example.com").setRole(Role.ROLE_CANDIDATE);
        CandidateDto candidateDto = new CandidateDto().setId(1).setUser(userDto);

        when(authentication.getPrincipal()).thenReturn(user);
        when(userService.getUserByEmail(user.getEmail())).thenReturn(userDto);
        when(candidateService.getCandidateByUserId(user.getId())).thenReturn(candidateDto);

        ResponseEntity<?> response = userController.getCurrentUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof CandidateDto);
        assertEquals(candidateDto, response.getBody());
    }

    @Test
    void getCurrentUser_WhenRoleIsCompany_ShouldReturnCompanyDto() {
        User user = new User().setId(1).setEmail("company@example.com").setRole(Role.ROLE_COMPANY);
        UserDto userDto = new UserDto().setId(1).setEmail("company@example.com").setRole(Role.ROLE_COMPANY);
        CompanyDto companyDto = new CompanyDto().setId(1).setUser(userDto);

        when(authentication.getPrincipal()).thenReturn(user);
        when(userService.getUserByEmail(user.getEmail())).thenReturn(userDto);
        when(companyService.getCompanyByUserId(user.getId())).thenReturn(companyDto);

        ResponseEntity<?> response = userController.getCurrentUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof CompanyDto);
        assertEquals(companyDto, response.getBody());
    }

    @Test
    void getCurrentUser_WhenRoleIsOther_ShouldReturnUserDto() {
        User user = new User().setId(1).setEmail("admin@example.com").setRole(Role.ROLE_ADMIN);
        UserDto userDto = new UserDto().setId(1).setEmail("admin@example.com").setRole(Role.ROLE_ADMIN);

        when(authentication.getPrincipal()).thenReturn(user);
        when(userService.getUserByEmail(user.getEmail())).thenReturn(userDto);

        ResponseEntity<?> response = userController.getCurrentUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserDto);
        assertEquals(userDto, response.getBody());
    }

    @Test
    void getAllRoles_ShouldReturnListOfRoles() {
        ResponseEntity<List<String>> response = userController.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsAll(Arrays.asList("ROLE_CANDIDATE", "ROLE_COMPANY", "ROLE_ADMIN")));
    }

    @Test
    void getCompanyJobs_WhenAuthorized_ShouldReturnJobs() {
        User user = new User().setId(1).setEmail("company@example.com").setRole(Role.ROLE_COMPANY);
        CompanyDto companyDto = new CompanyDto().setId(1).setUser(new UserDto().setId(1));
        Page<LoggedInCompanyJobDto> jobsPage = new PageImpl<>(List.of(new LoggedInCompanyJobDto()));

        when(authentication.getPrincipal()).thenReturn(user);
        when(companyService.getCompanyByUserId(user.getId())).thenReturn(companyDto);
        when(jobService.getLoggedInCompanyJobs(any(PageRequest.class), eq(companyDto.getId()))).thenReturn(jobsPage);

        ResponseEntity<?> response = userController.getCompanyJobs(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Page);
        assertEquals(jobsPage, response.getBody());
    }

    @Test
    void getCompanyJobs_WhenUnauthorized_ShouldReturnForbidden() {
        User user = new User().setId(1).setEmail("company@example.com").setRole(Role.ROLE_COMPANY);
        CompanyDto companyDto = new CompanyDto().setId(1).setUser(new UserDto().setId(2));

        when(authentication.getPrincipal()).thenReturn(user);
        when(companyService.getCompanyByUserId(user.getId())).thenReturn(companyDto);

        ResponseEntity<?> response = userController.getCompanyJobs(0, 10);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are not authorized to access this company's jobs", response.getBody());
    }
}
