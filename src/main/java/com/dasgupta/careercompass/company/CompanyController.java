package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.constants.Constants;
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

import java.util.Optional;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("")
    public Page<CompanyDto> getAllCompanies(@RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_NUMBER) int page, @RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_SIZE) int size) {
        log.info("getAllCompanies called with page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);

        return companyService.getAllCompanies(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable int id) {
        log.info("Fetching company with id: {}", id);
        Optional<CompanyDto> company = companyService.getCompanyById(id);

        if (company.isPresent()) {
            log.info("Company found: {}", company.get());
            return ResponseEntity.ok(company.get());
        } else {
            log.warn("Company not found with id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<CompanyReviewDto> createReview(@PathVariable int id, @RequestBody ReviewDto reviewDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Optional<CompanyReviewDto> resultReviewDto = companyService.createReview(id, reviewDto.getRating(), user);
        return resultReviewDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
