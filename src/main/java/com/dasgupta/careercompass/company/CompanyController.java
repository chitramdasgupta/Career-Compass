package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.company.companyReview.CompanyReviewDto;
import com.dasgupta.careercompass.company.companyReview.CompanyReviewService;
import com.dasgupta.careercompass.company.companyReview.ReviewDto;
import com.dasgupta.careercompass.constants.Constants;
import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.job.JobService;
import com.dasgupta.careercompass.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyService companyService;
    private final CompanyReviewService companyReviewService;
    private final JobService jobService;

    @Autowired
    public CompanyController(CompanyService companyService, JobService jobService,
                             CompanyReviewService companyReviewService) {
        this.companyService = companyService;
        this.jobService = jobService;
        this.companyReviewService = companyReviewService;
    }

    @GetMapping("")
    public Page<CompanyDto> getAllCompanies(@RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_NUMBER) int page,
                                            @RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_SIZE) int size) {
        log.info("getAllCompanies called with page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);

        return companyService.getAllCompanies(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable int id) {
        log.info("Fetching company with id: {}", id);

        CompanyDto company = companyService.getCompanyById(id);
        log.info("Company found: {}", company);

        return ResponseEntity.ok(company);
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<CompanyReviewDto> createReview(@PathVariable int id, @RequestBody ReviewDto reviewDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        CompanyReviewDto resultReviewDto = companyReviewService.createReview(id, user.getId(), reviewDto.getRating());

        return ResponseEntity.ok(resultReviewDto);
    }

    @GetMapping("/{id}/jobs")
    public Page<JobDto> getCompanyJobs(
            @PathVariable int id,
            @RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_SIZE) int size
    ) {
        log.info("getCompanyJobs called for company id={} with page={}, size={}", id, page, size);

        Pageable pageable = PageRequest.of(page, size);
        return jobService.getJobsByCompany(pageable, id);
    }
}
