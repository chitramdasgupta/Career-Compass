package com.dasgupta.careercompass.user;

import com.dasgupta.careercompass.candidate.CandidateDto;
import com.dasgupta.careercompass.candidate.CandidateService;
import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.company.CompanyService;
import com.dasgupta.careercompass.constants.Constants;
import com.dasgupta.careercompass.job.JobService;
import com.dasgupta.careercompass.job.LoggedInCompanyJobDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final CandidateService candidateService;
    private final CompanyService companyService;
    private final JobService jobService;

    @Autowired
    public UserController(UserService userService, CandidateService candidateService, CompanyService companyService,
                          JobService jobService) {
        this.userService = userService;
        this.candidateService = candidateService;
        this.companyService = companyService;
        this.jobService = jobService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        log.info("The user is: {}", user.getEmail());

        UserDto userDto = userService.getUserByEmail(user.getEmail());
        log.info("User found: {}", userDto.getEmail());

        return switch (userDto.getRole()) {
            case Role.ROLE_CANDIDATE -> {
                CandidateDto candidate = candidateService.getCandidateByUserId(user.getId());
                yield ResponseEntity.ok(candidate);
            }
            case Role.ROLE_COMPANY -> {
                CompanyDto company = companyService.getCompanyByUserId(user.getId());
                yield ResponseEntity.ok(company);
            }
            default -> ResponseEntity.ok(userDto);
        };
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAllRoles() {
        List<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toList());

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/me/jobs")
    public ResponseEntity<?> getCompanyJobs(
            @RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_SIZE) int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        CompanyDto company = companyService.getCompanyByUserId(user.getId());

        if (!Objects.equals(user.getId(), company.getUser().getId())) {
            log.info("The company whose jobs are requested is not authenticated");
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to access this company's jobs");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<LoggedInCompanyJobDto> jobs = jobService.getLoggedInCompanyJobs(pageable, company.getId());

        return ResponseEntity.ok(jobs);
    }
}
