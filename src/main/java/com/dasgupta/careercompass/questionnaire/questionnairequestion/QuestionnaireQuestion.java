package com.dasgupta.careercompass.questionnaire.questionnairequestion;

import com.dasgupta.careercompass.questionnaire.Questionnaire;
import com.dasgupta.careercompass.questionnaire.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

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

    // Boilerplate

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        QuestionnaireQuestion questionnaireQuestion = (QuestionnaireQuestion) o;
        return getId() != null && Objects.equals(getId(), questionnaireQuestion.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
