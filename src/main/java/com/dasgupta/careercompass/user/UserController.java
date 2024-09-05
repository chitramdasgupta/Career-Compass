package com.dasgupta.careercompass.user;

import com.dasgupta.careercompass.company.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final CandidateService candidateService;
    private final CompanyService companyService;

    @Autowired
    public UserController(UserService userService, CandidateService candidateService, CompanyService companyService) {
        this.userService = userService;
        this.candidateService = candidateService;
        this.companyService = companyService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Optional<UserDto> userDto = userService.getUserByEmail(user.getEmail());
        if (userDto.isPresent()) {
            log.info("User found: {}", userDto.get().getEmail());

            return switch (userDto.get().getRole()) {
                case Role.ROLE_CANDIDATE ->
                        ResponseEntity.ok(candidateService.getCandidateByUserId(userDto.get().getId())
                                .orElseThrow(() -> new RuntimeException("Candidate not found")));
                case Role.ROLE_COMPANY -> ResponseEntity.ok(companyService.getCompanyById(userDto.get().getId())
                        .orElseThrow(() -> new RuntimeException("Company not found")));
                default -> ResponseEntity.ok(userDto.get());
            };
        } else {
            log.info("User not found: {}", user.getEmail());
            
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAllRoles() {
        List<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toList());

        return ResponseEntity.ok(roles);
    }
}
