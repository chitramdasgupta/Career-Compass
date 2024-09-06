package com.dasgupta.careercompass.questionnaire.question;

import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private QuestionType type;

    @OneToMany(mappedBy = "question")
    @ToString.Exclude
    private Set<QuestionnaireQuestion> questionnaireQuestions;
}
