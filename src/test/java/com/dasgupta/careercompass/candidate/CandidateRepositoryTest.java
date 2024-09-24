package com.dasgupta.careercompass.candidate;

import com.dasgupta.careercompass.user.Role;
import com.dasgupta.careercompass.user.User;
import com.dasgupta.careercompass.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Candidate testCandidate;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        testUser = new User()
                .setEmail("test@example.com")
                .setPassword("password")
                .setRole(Role.ROLE_CANDIDATE);
        testUser = userRepository.save(testUser);

        // Create and save a test candidate
        testCandidate = new Candidate();
        testCandidate.setUser(testUser);
        testCandidate.setFirstName("John");
        testCandidate.setLastName("Doe");
        testCandidate = candidateRepository.save(testCandidate);
    }

    @Test
    void whenFindByUserId_thenReturnCandidate() {
        // when
        Optional<Candidate> found = candidateRepository.findByUserId(testUser.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(testCandidate.getId());
        assertThat(found.get().getUser().getId()).isEqualTo(testUser.getId());
        assertThat(found.get().getFirstName()).isEqualTo("John");
        assertThat(found.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    void whenFindByNonExistentUserId_thenReturnEmpty() {
        // when
        Optional<Candidate> found = candidateRepository.findByUserId(999999);

        // then
        assertThat(found).isEmpty();
    }
}
