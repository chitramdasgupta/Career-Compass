package com.dasgupta.careercompass.candidate;

import com.dasgupta.careercompass.bookmark.Bookmark;
import com.dasgupta.careercompass.company.companyReview.CompanyReview;
import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.jobApplication.JobApplication;
import com.dasgupta.careercompass.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@ToString
@RequiredArgsConstructor
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "candidate")
    @ToString.Exclude
    List<CompanyReview> reviews;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String firstName;

    private String middleName;

    private String lastName;

    @OneToMany(mappedBy = "candidate")
    @ToString.Exclude
    private Set<JobApplication> jobApplications;

    @OneToMany(mappedBy = "candidate")
    @ToString.Exclude
    private Set<Bookmark> bookmarks;

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
