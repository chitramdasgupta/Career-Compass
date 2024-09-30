package com.dasgupta.careercompass.user;

import com.dasgupta.careercompass.candidate.CandidateService;
import com.dasgupta.careercompass.company.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CandidateService candidateService;
    private final CompanyService companyService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                           CandidateService candidateService, CompanyService companyService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.candidateService = candidateService;
        this.companyService = companyService;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Searching for user with email: {}", email);

        return userRepository.findByEmail(email)
                .map(user -> {
                    log.info("Found user with email: {}", user.getEmail());
                    return userMapper.toDto(user);
                })
                .orElseThrow(() -> {
                    log.info("Could not find user with email: {}", email);
                    return new EntityNotFoundException("Could not find user with email: " + email);
                });
    }

    @Override
    public UserDto createUser(String email, String password, Role role, String name) {
        log.info("Attempting to create user with email = {}, role = {}", email, role);

        User user = new User()
                .setEmail(email)
                .setPassword(passwordEncoder.encode(password))
                .setRole(role);

        User savedUser = userRepository.save(user);
        log.info("Created user with email = {}, role = {}", savedUser.getEmail(), savedUser.getRole());

        switch (role) {
            case ROLE_CANDIDATE -> candidateService.createCandidate(savedUser);
            case ROLE_COMPANY -> companyService.createCompany(savedUser, name);
        }

        return userMapper.toDto(savedUser);
    }
}
