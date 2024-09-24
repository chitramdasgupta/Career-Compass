package com.dasgupta.careercompass.questionnaire.answer;

import com.dasgupta.careercompass.candidate.Candidate;
import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.jobApplication.JobApplication;
import com.dasgupta.careercompass.questionnaire.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

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

    // Boilerplate

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Job job = (Job) o;
        return getId() != null && Objects.equals(getId(), job.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
