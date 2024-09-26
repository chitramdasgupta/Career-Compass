package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.User;
import com.dasgupta.careercompass.user.UserDto;
import com.dasgupta.careercompass.user.UserMapper;
import com.dasgupta.careercompass.user.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final AuthResponseUserMapper authResponseUserMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     AuthResponseUserMapper authResponseUserMapper, UserService userService,
                                     UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.authResponseUserMapper = authResponseUserMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public AuthResponseUserDto register(AuthRequestUserDto input) {
        log.info("Registering a new user with email: {} and role: {}", input.getEmail(), input.getRole());

        UserDto user = userService.createUser(input.getEmail(), input.getPassword(), input.getRole(),
                input.getName());

        log.info("User created successfully");
        return authResponseUserMapper.toDto(userMapper.toEntity(user));
    }

    public User authenticate(AuthRequestUserDto input) {
        log.info("Authenticate called with email = {}, role = {}", input.getEmail(), input.getRole());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

        log.info("Authenticated user: {}", input.getEmail());
        return userMapper.toEntity(userService.getUserByEmail(input.getEmail()));
    }
}
