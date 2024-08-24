package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.jobApplication.JobApplication;
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
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private User user;

    @OneToOne(optional = false)
    private JobApplication jobApplication;

    @OneToOne(optional = false)
    private Question question;

    @Column(columnDefinition = "TEXT")
    private String response;
}
