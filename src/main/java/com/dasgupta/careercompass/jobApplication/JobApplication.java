package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.candidate.Candidate;
import com.dasgupta.careercompass.job.Job;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "job_application", indexes = {
        @Index(name = "idx_job_on_job_application", columnList = "job_id"),
        @Index(name = "idx_candidate_on_job_application", columnList = "candidate_id")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
}
