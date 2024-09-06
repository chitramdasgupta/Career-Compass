package com.dasgupta.careercompass.questionnaire.answer;

import com.dasgupta.careercompass.jobApplication.JobApplication;
import com.dasgupta.careercompass.questionnaire.question.Question;
import com.dasgupta.careercompass.user.Candidate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(optional = false)
    private Candidate candidate;

    @OneToOne(optional = false)
    private JobApplication jobApplication;

    @OneToOne(optional = false)
    private Question question;

    @Column(columnDefinition = "TEXT")
    private String response;
}
