package com.dasgupta.careercompass.config;

import com.dasgupta.careercompass.bookmark.Bookmark;
import com.dasgupta.careercompass.bookmark.BookmarkRepository;
import com.dasgupta.careercompass.candidate.Candidate;
import com.dasgupta.careercompass.candidate.CandidateRepository;
import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.company.CompanyRepository;
import com.dasgupta.careercompass.company.companyReview.CompanyReview;
import com.dasgupta.careercompass.company.companyReview.CompanyReviewRepository;
import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.job.JobLocation;
import com.dasgupta.careercompass.job.JobRepository;
import com.dasgupta.careercompass.job.JobStatus;
import com.dasgupta.careercompass.jobApplication.JobApplication;
import com.dasgupta.careercompass.jobApplication.JobApplicationRepository;
import com.dasgupta.careercompass.post.Post;
import com.dasgupta.careercompass.post.PostRepository;
import com.dasgupta.careercompass.questionnaire.Questionnaire;
import com.dasgupta.careercompass.questionnaire.QuestionnaireRepository;
import com.dasgupta.careercompass.questionnaire.answer.Answer;
import com.dasgupta.careercompass.questionnaire.answer.AnswerRepository;
import com.dasgupta.careercompass.questionnaire.question.Question;
import com.dasgupta.careercompass.questionnaire.question.QuestionRepository;
import com.dasgupta.careercompass.questionnaire.question.QuestionType;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestion;
import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionRepository;
import com.dasgupta.careercompass.user.Role;
import com.dasgupta.careercompass.user.User;
import com.dasgupta.careercompass.user.UserRepository;
import com.github.javafaker.Faker;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final QuestionRepository questionRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionnaireQuestionRepository questionnaireQuestionRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final AnswerRepository answerRepository;
    private final CompanyReviewRepository companyReviewRepository;
    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final Faker faker;

    public DataLoader(UserRepository userRepository, CandidateRepository candidateRepository,
                      CompanyRepository companyRepository, JobRepository jobRepository,
                      QuestionRepository questionRepository, QuestionnaireRepository questionnaireRepository,
                      QuestionnaireQuestionRepository questionnaireQuestionRepository,
                      JobApplicationRepository jobApplicationRepository, AnswerRepository answerRepository,
                      CompanyReviewRepository companyReviewRepository, BookmarkRepository bookmarkRepository,
                      PostRepository postRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.questionRepository = questionRepository;
        this.questionnaireRepository = questionnaireRepository;
        this.questionnaireQuestionRepository = questionnaireQuestionRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.answerRepository = answerRepository;
        this.companyReviewRepository = companyReviewRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
        this.faker = new Faker();
    }

    @Override
    public void run(String... args) {
        // Create users, candidates, and companies
        List<User> users = createUsers(50);
        List<Candidate> candidates = createCandidates(users.subList(0, 40));
        List<Company> companies = createCompanies(users.subList(40, 50));

        // Create jobs
        List<Job> jobs = createJobs(companies);

        // Create questions and questionnaires
        List<Question> questions = createQuestions();
        createQuestionnaires(jobs, questions);

        // Create job applications, answers, and reviews
        createJobApplications(candidates, jobs);
        createCompanyReviews(candidates, companies);

        // Create bookmarks
        createBookmarks(candidates, jobs);

        // Create posts
        createPosts();
    }

    private List<User> createUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User()
                    .setEmail(faker.internet().emailAddress())
                    .setPassword(passwordEncoder.encode("password"))
                    .setRole(i < 40 ? Role.ROLE_CANDIDATE : Role.ROLE_COMPANY);
            users.add(userRepository.save(user));
        }
        return users;
    }

    private List<Candidate> createCandidates(List<User> users) {
        List<Candidate> candidates = new ArrayList<>();
        for (User user : users) {
            Candidate candidate = new Candidate();
            candidate.setUser(user);
            candidate.setFirstName(faker.name().firstName());
            candidate.setLastName(faker.name().lastName());
            if (faker.bool().bool()) {
                candidate.setMiddleName(faker.name().firstName());
            }
            candidates.add(candidateRepository.save(candidate));
        }
        return candidates;
    }

    private List<Company> createCompanies(List<User> users) {
        List<Company> companies = new ArrayList<>();
        for (User user : users) {
            Company company = new Company();
            company.setUser(user);
            company.setName(faker.company().name());
            company.setDescription(faker.company().catchPhrase());
            companies.add(companyRepository.save(company));
        }
        return companies;
    }

    private List<Job> createJobs(List<Company> companies) {
        List<Job> jobs = new ArrayList<>();
        for (Company company : companies) {
            int jobCount = faker.number().numberBetween(1, 5);
            for (int i = 0; i < jobCount; i++) {
                Job job = new Job();
                job.setCompany(company);
                job.setTitle(faker.job().title());
                job.setDescription(faker.job().keySkills());
                job.setMinimumRequirement(faker.job().keySkills());
                job.setDesiredRequirement(faker.job().keySkills());
                job.setCity(faker.address().city());
                job.setCountry(CountryCode.getByCode(faker.address().countryCode()));
                job.setJobLocation(faker.options().option(JobLocation.class));
                job.setMinimumSalary(BigDecimal.valueOf(faker.number().numberBetween(30000, 80000)));
                job.setMaximumSalary(BigDecimal.valueOf(faker.number().numberBetween(80001, 150000)));
                job.setCurrency(CurrencyCode.USD);
                job.setStatus(faker.options().option(JobStatus.class));
                jobs.add(jobRepository.save(job));
            }
        }
        return jobs;
    }

    private List<Question> createQuestions() {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Question question = new Question();
            question.setText(faker.lorem().sentence() + "?");
            question.setType(faker.options().option(QuestionType.class));
            questions.add(questionRepository.save(question));
        }
        return questions;
    }

    private void createQuestionnaires(List<Job> jobs, List<Question> questions) {
        for (Job job : jobs) {
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setDescription("Questionnaire for " + job.getTitle());
            questionnaire.setJob(job);
            questionnaireRepository.save(questionnaire);

            List<Question> shuffledQuestions = new ArrayList<>(questions);
            Collections.shuffle(shuffledQuestions);
            int questionCount = faker.number().numberBetween(3, 7);

            for (int i = 0; i < questionCount && i < shuffledQuestions.size(); i++) {
                QuestionnaireQuestion qq = new QuestionnaireQuestion();
                qq.setQuestionnaire(questionnaire);
                qq.setQuestion(shuffledQuestions.get(i));
                qq.setDisplayOrder(i + 1);
                questionnaireQuestionRepository.save(qq);
            }
        }
    }

    private void createJobApplications(List<Candidate> candidates, List<Job> jobs) {
        for (Candidate candidate : candidates) {
            int applicationCount = faker.number().numberBetween(1, 5);
            List<Job> shuffledJobs = new ArrayList<>(jobs);
            Collections.shuffle(shuffledJobs);

            for (int i = 0; i < applicationCount && i < shuffledJobs.size(); i++) {
                Job job = shuffledJobs.get(i);
                JobApplication application = new JobApplication();
                application.setCandidate(candidate);
                application.setJob(job);
                jobApplicationRepository.save(application);

                // Create answers for the job's questionnaire
                if (job.getQuestionnaire() != null) {
                    for (QuestionnaireQuestion qq : job.getQuestionnaire().getQuestionnaireQuestions()) {
                        Answer answer = new Answer();
                        answer.setCandidate(candidate);
                        answer.setJobApplication(application);
                        answer.setQuestion(qq.getQuestion());
                        answer.setResponse(faker.lorem().paragraph());
                        answerRepository.save(answer);
                    }
                }
            }
        }
    }

    private void createCompanyReviews(List<Candidate> candidates, List<Company> companies) {
        for (Candidate candidate : candidates) {
            int reviewCount = faker.number().numberBetween(0, 3);
            List<Company> shuffledCompanies = new ArrayList<>(companies);
            Collections.shuffle(shuffledCompanies);

            for (int i = 0; i < reviewCount && i < shuffledCompanies.size(); i++) {
                Company company = shuffledCompanies.get(i);
                CompanyReview review = new CompanyReview();
                review.setCandidate(candidate);
                review.setCompany(company);
                review.setRating(faker.number().numberBetween(1, 5));
                companyReviewRepository.save(review);
            }
        }
    }

    private void createBookmarks(List<Candidate> candidates, List<Job> jobs) {
        for (Candidate candidate : candidates) {
            int bookmarkCount = faker.number().numberBetween(0, 5);
            List<Job> shuffledJobs = new ArrayList<>(jobs);
            Collections.shuffle(shuffledJobs);

            for (int i = 0; i < bookmarkCount && i < shuffledJobs.size(); i++) {
                Job job = shuffledJobs.get(i);
                Bookmark bookmark = new Bookmark();
                bookmark.setCandidate(candidate);
                bookmark.setJob(job);
                bookmarkRepository.save(bookmark);
            }
        }
    }


    private void createPosts() {
        for (int i = 0; i < 20; i++) {
            Post post = new Post();
            post.setTitle(faker.book().title());
            post.setContent(faker.lorem().paragraphs(3).stream().reduce((a, b) -> a + "\n\n" + b).orElse(""));
            postRepository.save(post);
        }
    }
}
