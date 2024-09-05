package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.company.CompanyRepository;
import com.dasgupta.careercompass.user.*;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthResponseUserMapper authResponseUserMapper;

    public AuthenticationServiceImpl(UserRepository userRepository, CandidateRepository candidateRepository, CompanyRepository companyRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, AuthResponseUserMapper authResponseUserMapper) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
        this.companyRepository = companyRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.authResponseUserMapper = authResponseUserMapper;
    }

    public AuthResponseUserDto register(AuthRequestUserDto input) {
        User user = new User().setEmail(input.getEmail()).setPassword(passwordEncoder.encode(input.getPassword())).setRole(input.getRole());

        User savedUser = userRepository.save(user);

        if (input.getRole() == Role.ROLE_CANDIDATE) {
            Candidate candidate = new Candidate();

            candidate.setUser(savedUser);
            candidateRepository.save(candidate);
        } else if (input.getRole() == Role.ROLE_COMPANY) {
            Company company = new Company();

            company.setUser(savedUser);
            company.setName(input.getName());
            companyRepository.save(company);
        }

        return authResponseUserMapper.toDto(savedUser);
    }

    public User authenticate(AuthRequestUserDto input) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }
}
