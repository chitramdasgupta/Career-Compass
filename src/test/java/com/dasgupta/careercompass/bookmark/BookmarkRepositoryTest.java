package com.dasgupta.careercompass.bookmark;

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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    private Candidate testCandidate;
    private Job testJob;

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

        // Create and save some bookmarks
        for (int i = 0; i < 5; i++) {
            Job job = new Job();
            job.setTitle("Job " + i);
            job.setDescription("Description for Job " + i);
            job.setCompany(company);
            job.setCountry(CountryCode.US);
            job.setJobLocation(JobLocation.REMOTE);
            job.setStatus(JobStatus.QUESTIONNAIRE_PENDING);
            job = jobRepository.save(job);

            Bookmark bookmark = new Bookmark();
            bookmark.setCandidate(testCandidate);
            bookmark.setJob(job);
            bookmarkRepository.save(bookmark);
        }
    }

    @Test
    void whenFindByCandidateId_thenReturnBookmarkPage() {
        // when
        Page<Bookmark> bookmarkPage = bookmarkRepository.findByCandidateId(testCandidate.getId(), PageRequest.of(0, 10));

        // then
        assertThat(bookmarkPage).isNotEmpty();
        assertThat(bookmarkPage.getContent()).hasSize(5);
        assertThat(bookmarkPage.getContent()).allSatisfy(bookmark -> {
            assertThat(bookmark.getCandidate().getId()).isEqualTo(testCandidate.getId());
            assertThat(bookmark.getJob().getTitle()).startsWith("Job ");
        });
    }

    @Test
    void whenFindByNonExistentCandidateId_thenReturnEmptyPage() {
        // when
        Page<Bookmark> bookmarkPage = bookmarkRepository.findByCandidateId(999999, PageRequest.of(0, 10));

        // then
        assertThat(bookmarkPage).isEmpty();
    }

    @Test
    void whenFindByCandidateIdAndJobId_thenReturnBookmark() {
        // when
        Optional<Bookmark> found = bookmarkRepository.findByCandidateIdAndJobId(testCandidate.getId(), testJob.getId());

        // then
        assertThat(found).isEmpty();

        // Create a new bookmark
        Bookmark newBookmark = new Bookmark();
        newBookmark.setCandidate(testCandidate);
        newBookmark.setJob(testJob);
        bookmarkRepository.save(newBookmark);

        // when
        found = bookmarkRepository.findByCandidateIdAndJobId(testCandidate.getId(), testJob.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getCandidate().getId()).isEqualTo(testCandidate.getId());
        assertThat(found.get().getJob().getId()).isEqualTo(testJob.getId());
    }

    @Test
    void whenExistsByCandidateIdAndJobId_thenReturnTrue() {
        // Create a new bookmark
        Bookmark newBookmark = new Bookmark();
        newBookmark.setCandidate(testCandidate);
        newBookmark.setJob(testJob);
        bookmarkRepository.save(newBookmark);

        // when
        boolean exists = bookmarkRepository.existsByCandidateIdAndJobId(testCandidate.getId(), testJob.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void whenNotExistsByCandidateIdAndJobId_thenReturnFalse() {
        // when
        boolean exists = bookmarkRepository.existsByCandidateIdAndJobId(testCandidate.getId(), testJob.getId());

        // then
        assertThat(exists).isFalse();
    }
}
