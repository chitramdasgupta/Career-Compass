package com.dasgupta.careercompass.user;

import com.dasgupta.careercompass.bookmark.Bookmark;
import com.dasgupta.careercompass.company.CompanyReview;
import com.dasgupta.careercompass.jobApplication.JobApplication;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", indexes = {@Index(name = "idx_user_on_email", columnList = "email")})
@Getter
@Setter
@Accessors(chain = true)
@ToString
@RequiredArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<JobApplication> jobApplications;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    List<CompanyReview> reviews;

    private String firstName;

    private String middleName;

    private String lastName;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<Bookmark> bookmarks;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private Role role;

    // Returns the user's roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
