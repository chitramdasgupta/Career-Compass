package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.user.Candidate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@ToString
@RequiredArgsConstructor
public class CompanyReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @NotNull
    @Column(nullable = false)
    private Integer rating;
}