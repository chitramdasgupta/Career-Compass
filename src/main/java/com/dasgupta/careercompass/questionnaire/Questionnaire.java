package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.job.Job;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany
    @ToString.Exclude
    private Set<Question> questions;

    @OneToOne(optional = false)
    @JoinColumn(unique = true)
    private Job job;
}
