package com.dasgupta.careercompass.user;

import com.dasgupta.careercompass.candidate.CandidateService;
import com.dasgupta.careercompass.company.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CandidateService candidateService;
    @Mock
    private CompanyService companyService;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper, passwordEncoder, candidateService, companyService);
    }

    @Test
    void getUserByEmail_WhenUserExists_ShouldReturnUserDto() {
        // Arrange
        String email = "test@example.com";
        User user = new User().setEmail(email);
        UserDto userDto = new UserDto();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act
        UserDto result = userService.getUserByEmail(email);

        // Assert
        assertNotNull(result);
        verify(userRepository).findByEmail(email);
        verify(userMapper).toDto(user);
    }

    @Test
    void getUserByEmail_WhenUserDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail(email));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void createUser_WhenRoleIsCandidate_ShouldCreateUserAndCandidate() {
        // Arrange
        String email = "candidate@example.com";
        String password = "password";
        Role role = Role.ROLE_CANDIDATE;
        String name = "John Doe";

        User user = new User().setEmail(email).setRole(role);
        UserDto userDto = new UserDto();

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act
        UserDto result = userService.createUser(email, password, role, name);

        // Assert
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(password);
        verify(candidateService).createCandidate(user);
        verify(userMapper).toDto(user);
    }

    @Test
    void createUser_WhenRoleIsCompany_ShouldCreateUserAndCompany() {
        // Arrange
        String email = "company@example.com";
        String password = "password";
        Role role = Role.ROLE_COMPANY;
        String name = "Acme Inc.";

        User user = new User().setEmail(email).setRole(role);
        UserDto userDto = new UserDto();

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act
        UserDto result = userService.createUser(email, password, role, name);

        // Assert
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(password);
        verify(companyService).createCompany(user, name);
        verify(userMapper).toDto(user);
    }
}
