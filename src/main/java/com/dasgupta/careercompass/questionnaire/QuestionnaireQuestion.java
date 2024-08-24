package com.dasgupta.careercompass.questionnaire;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"questionnaire_id", "order"})
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class QuestionnaireQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Questionnaire questionnaire;

    @ManyToOne
    private Question question;

    private Integer order;
}
