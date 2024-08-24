package com.dasgupta.careercompass.questionnaire;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "questionnaire_question", uniqueConstraints = @UniqueConstraint(columnNames = {"questionnaire_id", "display_order"}))
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class QuestionnaireQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private Questionnaire questionnaire;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    private Integer displayOrder;
}
