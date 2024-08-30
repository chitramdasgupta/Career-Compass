package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
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

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<QuestionnaireQuestion> questionnaireQuestions = new HashSet<>();

    @OneToOne(optional = false)
    @JoinColumn(unique = true)
    private Job job;
}
