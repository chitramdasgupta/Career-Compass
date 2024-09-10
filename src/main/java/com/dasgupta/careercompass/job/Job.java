package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.bookmark.Bookmark;
import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.jobApplication.JobApplication;
import com.dasgupta.careercompass.questionnaire.Questionnaire;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "job", indexes = {@Index(name = "idx_job_on_company_id", columnList = "company_id")})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String minimumRequirement;

    @Column(columnDefinition = "TEXT")
    private String desiredRequirement;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Company company;

    private String city;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CountryCode country;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private JobLocation jobLocation;

    private BigDecimal minimumSalary;

    private BigDecimal maximumSalary;

    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;

    @OneToMany(mappedBy = "job")
    @ToString.Exclude
    private Set<JobApplication> jobApplications;

    @OneToOne(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private Questionnaire questionnaire;

    @OneToMany(mappedBy = "job")
    @ToString.Exclude
    private Set<Bookmark> bookmarks;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    @ColumnDefault("'QUESTIONNAIRE_PENDING'")
    private JobStatus status;

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
