package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("jobs")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("")
    public Page<JobDto> getAllJobs(@RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_NUMBER) int page, @RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jobService.getAllJobs(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable int id) {
        Optional<JobDto> jobDto = jobService.getJobById(id);

        return jobDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
