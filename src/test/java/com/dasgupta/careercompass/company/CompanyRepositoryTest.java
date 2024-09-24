package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.user.User;
import com.dasgupta.careercompass.user.Role;
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
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Company testCompany;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        testUser = new User()
                .setEmail("company@example.com")
                .setPassword("password")
                .setRole(Role.ROLE_COMPANY);
        testUser = userRepository.save(testUser);

        // Create and save a test company
        testCompany = new Company();
        testCompany.setUser(testUser);
        testCompany.setName("Test Company");
        testCompany.setDescription("This is a test company");
        testCompany = companyRepository.save(testCompany);
    }

    @Test
    void whenFindByUserId_thenReturnCompany() {
        // when
        Optional<Company> found = companyRepository.findByUserId(testUser.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(testCompany.getId());
        assertThat(found.get().getUser().getId()).isEqualTo(testUser.getId());
        assertThat(found.get().getName()).isEqualTo("Test Company");
        assertThat(found.get().getDescription()).isEqualTo("This is a test company");
    }

    @Test
    void whenFindByNonExistentUserId_thenReturnEmpty() {
        // when
        Optional<Company> found = companyRepository.findByUserId(999999);

        // then
        assertThat(found).isEmpty();
    }
}
