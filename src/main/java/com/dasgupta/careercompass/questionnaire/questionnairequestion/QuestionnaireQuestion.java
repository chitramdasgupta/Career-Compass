package com.dasgupta.careercompass.questionnaire.questionnairequestion;

import com.dasgupta.careercompass.questionnaire.Questionnaire;
import com.dasgupta.careercompass.questionnaire.question.Question;
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
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private Questionnaire questionnaire;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private Question question;

    private Integer displayOrder;
}
