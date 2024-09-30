package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthResponseUserMapper authResponseUserMapper;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationServiceImpl(authenticationManager, authResponseUserMapper,
                userService, userMapper);
    }

    @Test
    void register_ShouldCreateUserAndReturnAuthResponseUserDto() {
        // Arrange
        AuthRequestUserDto input = new AuthRequestUserDto();
        input.setEmail("test@example.com");
        input.setPassword("password");
        input.setRole(Role.ROLE_CANDIDATE);
        input.setName("Test User");

        UserDto createdUserDto = new UserDto();
        createdUserDto.setEmail(input.getEmail());
        createdUserDto.setRole(input.getRole());

        User createdUser = new User();
        createdUser.setEmail(input.getEmail());
        createdUser.setRole(input.getRole());

        AuthResponseUserDto expectedResponse = new AuthResponseUserDto();
        expectedResponse.setEmail(input.getEmail());
        expectedResponse.setRole(input.getRole());

        when(userService.createUser(input.getEmail(), input.getPassword(), input.getRole(), input.getName()))
                .thenReturn(createdUserDto);
        when(userMapper.toEntity(createdUserDto)).thenReturn(createdUser);
        when(authResponseUserMapper.toDto(createdUser)).thenReturn(expectedResponse);

        // Act
        AuthResponseUserDto result = authenticationService.register(input);

        // Assert
        assertNotNull(result);
        assertEquals(input.getEmail(), result.getEmail());
        assertEquals(input.getRole(), result.getRole());
        verify(userService).createUser(input.getEmail(), input.getPassword(), input.getRole(), input.getName());
        verify(userMapper).toEntity(createdUserDto);
        verify(authResponseUserMapper).toDto(createdUser);
    }

    @Test
    void authenticate_ShouldAuthenticateAndReturnUser() {
        // Arrange
        AuthRequestUserDto input = new AuthRequestUserDto();
        input.setEmail("test@example.com");
        input.setPassword("password");
        input.setRole(Role.ROLE_CANDIDATE);

        UserDto authenticatedUserDto = new UserDto();
        authenticatedUserDto.setEmail(input.getEmail());
        authenticatedUserDto.setRole(input.getRole());

        User authenticatedUser = new User();
        authenticatedUser.setEmail(input.getEmail());
        authenticatedUser.setRole(input.getRole());

        when(userService.getUserByEmail(input.getEmail())).thenReturn(authenticatedUserDto);
        when(userMapper.toEntity(authenticatedUserDto)).thenReturn(authenticatedUser);

        // Act
        User result = authenticationService.authenticate(input);

        // Assert
        assertNotNull(result);
        assertEquals(input.getEmail(), result.getEmail());
        assertEquals(input.getRole(), result.getRole());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).getUserByEmail(input.getEmail());
        verify(userMapper).toEntity(authenticatedUserDto);
    }
}
