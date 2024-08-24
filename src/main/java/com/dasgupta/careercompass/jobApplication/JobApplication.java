package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Job_application", indexes = {
        @Index(name = "idx_job_on_job_application", columnList = "job_id"),
        @Index(name = "idx_user_on_job_application", columnList = "user_id")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Job job;

    @ManyToOne
    @JoinColumn
    private User user;
}
