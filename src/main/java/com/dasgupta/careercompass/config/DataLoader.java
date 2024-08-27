package com.dasgupta.careercompass.config;

import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.company.CompanyRepository;
import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.job.JobLocation;
import com.dasgupta.careercompass.job.JobRepository;
import com.dasgupta.careercompass.post.Post;
import com.dasgupta.careercompass.post.PostRepository;
import com.dasgupta.careercompass.questionnaire.Questionnaire;
import com.dasgupta.careercompass.questionnaire.QuestionnaireQuestion;
import com.dasgupta.careercompass.questionnaire.question.Question;
import com.dasgupta.careercompass.questionnaire.question.QuestionRepository;
import com.dasgupta.careercompass.questionnaire.question.QuestionType;
import com.github.javafaker.Faker;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataLoader {
    private static final int NUM_COMPANIES = 100;
    private static final int NUM_JOBS_PER_COMPANY = 25;
    private static final int NUM_POSTS = 100;
    private static final int NUM_QUESTIONS = 10;

    private final Faker faker = new Faker();

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final PostRepository postRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public DataLoader(CompanyRepository companyRepository, JobRepository jobRepository, PostRepository postRepository, QuestionRepository questionRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.postRepository = postRepository;
        this.questionRepository = questionRepository;
    }

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            loadCompanies();
            loadQuestions();
            loadJobs();
            loadPosts();
        };
    }

    private void loadCompanies() {
        if (companyRepository.count() == 0) {
            for (int i = 0; i < NUM_COMPANIES; i++) {
                Company company = new Company();
                company.setName(faker.company().name());
                company.setDescription(faker.company().catchPhrase());

                companyRepository.save(company);
            }
        }
    }


    private void loadQuestions() {
        for (int i = 0; i < NUM_QUESTIONS; i++) {
            Question question = new Question();
            question.setText(faker.lorem().sentence());
            question.setType(QuestionType.values()[faker.random().nextInt(QuestionType.values().length)]);
            questionRepository.save(question);
        }
    }

    private void loadJobs() {
        if (jobRepository.count() == 0) {
            List<Company> companies = companyRepository.findAll();
            List<Question> questions = questionRepository.findAll();

            for (Company company : companies) {
                for (int i = 0; i < NUM_JOBS_PER_COMPANY; i++) {
                    Job job = new Job();
                    job.setTitle(faker.job().title());

                    String description = faker.lorem().paragraph() + "\n\n" + faker.lorem().paragraph();
                    job.setDescription(description);

                    if (faker.random().nextBoolean()) {
                        job.setMinimumRequirement(faker.lorem().paragraph());
                    }
                    if (faker.random().nextBoolean()) {
                        job.setDesiredRequirement(faker.lorem().paragraph());
                    }

                    job.setCompany(company);
                    job.setJobLocation(JobLocation.values()[faker.random().nextInt(JobLocation.values().length)]);
                    job.setCountry(faker.options().option(CountryCode.class));
                    job.setMinimumSalary(BigDecimal.valueOf(faker.number().randomDouble(2, 30000, 50000)));
                    job.setMaximumSalary(BigDecimal.valueOf(faker.number().randomDouble(2, 60000, 120000)));
                    job.setCurrency(faker.options().option(CurrencyCode.class));

                    Questionnaire questionnaire = new Questionnaire();
                    questionnaire.setDescription(faker.lorem().sentence());
                    questionnaire.setJob(job);
                    job.setQuestionnaire(questionnaire);

                    for (int j = 0; j < NUM_QUESTIONS; j++) {
                        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
                        questionnaireQuestion.setQuestionnaire(questionnaire);
                        questionnaireQuestion.setQuestion(questions.get(faker.random().nextInt(questions.size())));
                        questionnaireQuestion.setDisplayOrder(j);
                        questionnaire.getQuestionnaireQuestions().add(questionnaireQuestion);
                    }

                    jobRepository.save(job);
                }
            }
        }
    }

    private void loadPosts() {
        if (postRepository.count() == 0) {
            for (int i = 0; i < NUM_POSTS; i++) {
                Post post = new Post();
                post.setTitle(faker.book().title());
                post.setContent(faker.lorem().paragraphs(3).stream().reduce((a, b) -> a + "\n\n" + b).orElse(""));

                postRepository.save(post);
            }
        }
    }
}
