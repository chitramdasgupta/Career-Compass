package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.candidate.Candidate;
import com.dasgupta.careercompass.candidate.CandidateRepository;
import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.company.CompanyRepository;
import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.job.JobLocation;
import com.dasgupta.careercompass.job.JobRepository;
import com.dasgupta.careercompass.job.JobStatus;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class JobApplicationRepositoryTest {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    private Job testJob;
    private Candidate testCandidate;

    @BeforeEach
    void setUp() {
        // Create and save a test company
        User companyUser = new User()
                .setEmail("company@example.com")
                .setPassword("password")
                .setRole(Role.ROLE_COMPANY);
        companyUser = userRepository.save(companyUser);

        Company company = new Company();
        company.setUser(companyUser);
        company.setName("Test Company");
        company = companyRepository.save(company);

        // Create and save a test job
        testJob = new Job();
        testJob.setTitle("Test Job");
        testJob.setDescription("Test Job Description");
        testJob.setCompany(company);
        testJob.setCountry(CountryCode.US);
        testJob.setJobLocation(JobLocation.REMOTE);
        testJob.setStatus(JobStatus.QUESTIONNAIRE_PENDING);
        testJob = jobRepository.save(testJob);

        // Create and save a test candidate
        User candidateUser = new User()
                .setEmail("candidate@example.com")
                .setPassword("password")
                .setRole(Role.ROLE_CANDIDATE);
        candidateUser = userRepository.save(candidateUser);

        testCandidate = new Candidate();
        testCandidate.setUser(candidateUser);
        testCandidate.setFirstName("John");
        testCandidate.setLastName("Doe");
        testCandidate = candidateRepository.save(testCandidate);

        // Create and save some job applications
        for (int i = 0; i < 5; i++) {
            JobApplication jobApplication = new JobApplication();
            jobApplication.setJob(testJob);
            jobApplication.setCandidate(testCandidate);
            jobApplicationRepository.save(jobApplication);
        }
    }

    @Test
    void whenFindByJobId_thenReturnJobApplicationPage() {
        // when
        Page<JobApplication> jobApplicationPage = jobApplicationRepository.findByJobId(testJob.getId(), PageRequest.of(0, 10));

        // then
        assertThat(jobApplicationPage).isNotEmpty();
        assertThat(jobApplicationPage.getContent()).hasSize(5);
        assertThat(jobApplicationPage.getContent()).allSatisfy(jobApplication -> {
            assertThat(jobApplication.getJob().getId()).isEqualTo(testJob.getId());
            assertThat(jobApplication.getCandidate().getId()).isEqualTo(testCandidate.getId());
        });
    }

    @Test
    void whenFindByNonExistentJobId_thenReturnEmptyPage() {
        // when
        Page<JobApplication> jobApplicationPage = jobApplicationRepository.findByJobId(999999, PageRequest.of(0, 10));

        // then
        assertThat(jobApplicationPage).isEmpty();
    }

    @Test
    void whenFindByCandidateId_thenReturnJobPage() {
        // when
        Page<Job> jobPage = jobApplicationRepository.findByCandidateId(testCandidate.getId(), PageRequest.of(0, 10));

        // then
        assertThat(jobPage).isNotEmpty();
        assertThat(jobPage.getContent()).hasSize(1);
        assertThat(jobPage.getContent().get(0).getId()).isEqualTo(testJob.getId());
    }

    @Test
    void whenFindByNonExistentCandidateId_thenReturnEmptyPage() {
        // when
        Page<Job> jobPage = jobApplicationRepository.findByCandidateId(999999, PageRequest.of(0, 10));

        // then
        assertThat(jobPage).isEmpty();
    }
}
