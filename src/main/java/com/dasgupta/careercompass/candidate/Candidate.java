package com.dasgupta.careercompass.candidate;

import com.dasgupta.careercompass.bookmark.Bookmark;
import com.dasgupta.careercompass.company.CompanyReview;
import com.dasgupta.careercompass.jobApplication.JobApplication;
import com.dasgupta.careercompass.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
    List<CompanyReview> reviews;

    @OneToMany(mappedBy = "candidate")
    @ToString.Exclude
    private Set<Bookmark> bookmarks;
}
