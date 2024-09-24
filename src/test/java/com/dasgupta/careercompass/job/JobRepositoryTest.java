package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.company.CompanyRepository;
import com.dasgupta.careercompass.user.User;
import com.dasgupta.careercompass.user.Role;
import com.dasgupta.careercompass.user.UserRepository;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    private Company testCompany;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        User testUser = new User()
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

        // Create and save some test jobs
        for (int i = 0; i < 5; i++) {
            Job job = new Job();
            job.setTitle("Job " + i);
            job.setDescription("Description for Job " + i);
            job.setCompany(testCompany);
            job.setCountry(CountryCode.US);
            job.setJobLocation(JobLocation.REMOTE);
            job.setStatus(JobStatus.QUESTIONNAIRE_PENDING);
            job.setMinimumSalary(BigDecimal.valueOf(50000));
            job.setMaximumSalary(BigDecimal.valueOf(100000));
            jobRepository.save(job);
        }
    }

    @Test
    void whenFindByCompanyId_thenReturnJobPage() {
        // when
        Page<Job> jobPage = jobRepository.findByCompanyId(testCompany.getId(), PageRequest.of(0, 10));

        // then
        assertThat(jobPage).isNotEmpty();
        assertThat(jobPage.getContent()).hasSize(5);
        assertThat(jobPage.getContent()).allSatisfy(job -> {
            assertThat(job.getCompany().getId()).isEqualTo(testCompany.getId());
            assertThat(job.getTitle()).startsWith("Job ");
            assertThat(job.getDescription()).startsWith("Description for Job ");
            assertThat(job.getCountry()).isEqualTo(CountryCode.US);
            assertThat(job.getJobLocation()).isEqualTo(JobLocation.REMOTE);
            assertThat(job.getStatus()).isEqualTo(JobStatus.QUESTIONNAIRE_PENDING);
            assertThat(job.getMinimumSalary()).isEqualByComparingTo(BigDecimal.valueOf(50000));
            assertThat(job.getMaximumSalary()).isEqualByComparingTo(BigDecimal.valueOf(100000));
        });
    }

    @Test
    void whenFindByNonExistentCompanyId_thenReturnEmptyPage() {
        // when
        Page<Job> jobPage = jobRepository.findByCompanyId(999999, PageRequest.of(0, 10));

        // then
        assertThat(jobPage).isEmpty();
    }

    @Test
    void whenFindByCompanyId_thenPaginationWorks() {
        // Create 10 more jobs
        for (int i = 5; i < 15; i++) {
            Job job = new Job();
            job.setTitle("Job " + i);
            job.setDescription("Description for Job " + i);
            job.setCompany(testCompany);
            job.setCountry(CountryCode.US);
            job.setJobLocation(JobLocation.REMOTE);
            job.setStatus(JobStatus.QUESTIONNAIRE_PENDING);
            jobRepository.save(job);
        }

        // when
        Page<Job> firstPage = jobRepository.findByCompanyId(testCompany.getId(), PageRequest.of(0, 5));
        Page<Job> secondPage = jobRepository.findByCompanyId(testCompany.getId(), PageRequest.of(1, 5));
        Page<Job> thirdPage = jobRepository.findByCompanyId(testCompany.getId(), PageRequest.of(2, 5));

        // then
        assertThat(firstPage).hasSize(5);
        assertThat(secondPage).hasSize(5);
        assertThat(thirdPage).hasSize(5);
        assertThat(firstPage.getContent()).extracting("title")
                .containsExactly("Job 0", "Job 1", "Job 2", "Job 3", "Job 4");
        assertThat(secondPage.getContent()).extracting("title")
                .containsExactly("Job 5", "Job 6", "Job 7", "Job 8", "Job 9");
        assertThat(thirdPage.getContent()).extracting("title")
                .containsExactly("Job 10", "Job 11", "Job 12", "Job 13", "Job 14");
    }
}
