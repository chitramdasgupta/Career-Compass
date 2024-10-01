package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.User;
import com.dasgupta.careercompass.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private Environment env;

    @Mock
    private AuthResponseUserMapper authResponseUserMapper;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterCandidate() {
        // Arrange
        AuthRequestUserDto requestDto = new AuthRequestUserDto();
        requestDto.setEmail("candidate@example.com");
        requestDto.setPassword("password");
        requestDto.setRole(Role.ROLE_CANDIDATE);

        AuthResponseUserDto responseDto = new AuthResponseUserDto();
        responseDto.setEmail("candidate@example.com");
        responseDto.setRole(Role.ROLE_CANDIDATE);

        when(authenticationService.register(requestDto)).thenReturn(responseDto);

        // Act
        ResponseEntity<AuthResponseUserDto> response = authenticationController.register(requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(authenticationService).register(requestDto);
    }

    @Test
    void testRegisterCompany() {
        // Arrange
        AuthRequestUserDto requestDto = new AuthRequestUserDto();
        requestDto.setEmail("company@example.com");
        requestDto.setPassword("password");
        requestDto.setRole(Role.ROLE_COMPANY);
        requestDto.setName("Test Company");

        AuthResponseUserDto responseDto = new AuthResponseUserDto();
        responseDto.setEmail("company@example.com");
        responseDto.setRole(Role.ROLE_COMPANY);

        when(authenticationService.register(requestDto)).thenReturn(responseDto);

        // Act
        ResponseEntity<AuthResponseUserDto> response = authenticationController.register(requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(authenticationService).register(requestDto);
    }

    @Test
    void testAuthenticateCandidate() {
        // Arrange
        AuthRequestUserDto requestDto = new AuthRequestUserDto();
        requestDto.setEmail("candidate@example.com");
        requestDto.setPassword("password");

        User authenticatedUser = new User();
        authenticatedUser.setEmail("candidate@example.com");
        authenticatedUser.setRole(Role.ROLE_CANDIDATE);

        AuthResponseUserDto responseDto = new AuthResponseUserDto();
        responseDto.setEmail("candidate@example.com");
        responseDto.setRole(Role.ROLE_CANDIDATE);

        when(authenticationService.authenticate(requestDto)).thenReturn(authenticatedUser);
        when(authResponseUserMapper.toDto(authenticatedUser)).thenReturn(responseDto);
        when(jwtService.generateToken(authenticatedUser)).thenReturn("jwt-token");
        when(env.getActiveProfiles()).thenReturn(new String[]{"dev"});

        // Act
        ResponseEntity<AuthResponseUserDto> response = authenticationController.authenticate(requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
        String cookieHeader = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookieHeader, "Cookie header should not be null");
        assertTrue(cookieHeader.contains("jwt=jwt-token"), "Cookie should contain JWT token");
        assertTrue(cookieHeader.contains("HttpOnly"), "Cookie should be HttpOnly");
        assertFalse(cookieHeader.contains("Secure"), "Cookie should not be Secure in dev environment");

        verify(authenticationService).authenticate(requestDto);
        verify(jwtService).generateToken(authenticatedUser);
        verify(env).getActiveProfiles();
    }

    @Test
    void testAuthenticateCompany() {
        // Arrange
        AuthRequestUserDto requestDto = new AuthRequestUserDto();
        requestDto.setEmail("company@example.com");
        requestDto.setPassword("password");

        User authenticatedUser = new User();
        authenticatedUser.setEmail("company@example.com");
        authenticatedUser.setRole(Role.ROLE_COMPANY);

        AuthResponseUserDto responseDto = new AuthResponseUserDto();
        responseDto.setEmail("company@example.com");
        responseDto.setRole(Role.ROLE_COMPANY);

        when(authenticationService.authenticate(requestDto)).thenReturn(authenticatedUser);
        when(authResponseUserMapper.toDto(authenticatedUser)).thenReturn(responseDto);
        when(jwtService.generateToken(authenticatedUser)).thenReturn("jwt-token");
        when(env.getActiveProfiles()).thenReturn(new String[]{"dev"});

        // Act
        ResponseEntity<AuthResponseUserDto> response = authenticationController.authenticate(requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
        String cookieHeader = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookieHeader, "Cookie header should not be null");
        assertTrue(cookieHeader.contains("jwt=jwt-token"), "Cookie should contain JWT token");
        assertTrue(cookieHeader.contains("HttpOnly"), "Cookie should be HttpOnly");
        assertFalse(cookieHeader.contains("Secure"), "Cookie should not be Secure in dev environment");

        verify(authenticationService).authenticate(requestDto);
        verify(jwtService).generateToken(authenticatedUser);
        verify(env).getActiveProfiles();
    }

    @Test
    void testLogout() {
        // Arrange
        when(env.getActiveProfiles()).thenReturn(new String[]{"dev"});

        // Act
        ResponseEntity<Void> response = authenticationController.logout();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
        String cookieHeader = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookieHeader, "Cookie header should not be null");
        assertTrue(cookieHeader.contains("jwt="), "Cookie should contain jwt=");
        assertTrue(cookieHeader.contains("Max-Age=0"), "Cookie should have Max-Age=0");
        assertTrue(cookieHeader.contains("HttpOnly"), "Cookie should be HttpOnly");
        assertFalse(cookieHeader.contains("Secure"), "Cookie should not be Secure in dev environment");

        verify(env).getActiveProfiles();
    }

    @Test
    void testAuthenticateInProdEnvironment() {
        // Arrange
        AuthRequestUserDto requestDto = new AuthRequestUserDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");

        User authenticatedUser = new User();
        authenticatedUser.setEmail("test@example.com");

        AuthResponseUserDto responseDto = new AuthResponseUserDto();
        responseDto.setEmail("test@example.com");
        responseDto.setRole(Role.ROLE_CANDIDATE);

        when(authenticationService.authenticate(requestDto)).thenReturn(authenticatedUser);
        when(authResponseUserMapper.toDto(authenticatedUser)).thenReturn(responseDto);
        when(jwtService.generateToken(authenticatedUser)).thenReturn("jwt-token");
        when(env.getActiveProfiles()).thenReturn(new String[]{"prod"});

        // Act
        ResponseEntity<AuthResponseUserDto> response = authenticationController.authenticate(requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
        String cookieHeader = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookieHeader, "Cookie header should not be null");
        assertTrue(cookieHeader.contains("jwt=jwt-token"), "Cookie should contain JWT token");
        assertTrue(cookieHeader.contains("HttpOnly"), "Cookie should be HttpOnly");
        assertTrue(cookieHeader.contains("Secure"), "Cookie should be Secure in prod environment");

        verify(authenticationService).authenticate(requestDto);
        verify(jwtService).generateToken(authenticatedUser);
        verify(env).getActiveProfiles();
    }
}
