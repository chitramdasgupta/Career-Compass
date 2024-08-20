package com.dasgupta.careercompass.config;

import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.company.CompanyRepository;
import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.job.JobLocation;
import com.dasgupta.careercompass.job.JobRepository;
import com.dasgupta.careercompass.post.Post;
import com.dasgupta.careercompass.post.PostRepository;
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

    private final Faker faker = new Faker();

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final PostRepository postRepository;

    @Autowired
    public DataLoader(CompanyRepository companyRepository, JobRepository jobRepository, PostRepository postRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.postRepository = postRepository;
    }

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            loadCompanies();
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

    private void loadJobs() {
        if (jobRepository.count() == 0) {
            List<Company> companies = companyRepository.findAll();

            for (Company company : companies) {
                for (int i = 0; i < NUM_JOBS_PER_COMPANY; i++) {
                    Job job = new Job();
                    job.setTitle(faker.job().title());

                    String description = faker.lorem().paragraph() + "\n\n" +
                            faker.lorem().paragraph() + "\n\n" +
                            faker.lorem().paragraph();
                    job.setDescription(description);

                    job.setCompany(company);

                    job.setJobLocation(JobLocation.values()[faker.random().nextInt(JobLocation.values().length)]);
                    job.setCountry(faker.options().option(CountryCode.class));
                    job.setMinimumSalary(BigDecimal.valueOf(faker.number().randomDouble(2, 30000, 50000)));
                    job.setMaximumSalary(BigDecimal.valueOf(faker.number().randomDouble(2, 60000, 120000)));
                    job.setCurrency(faker.options().option(CurrencyCode.class));

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
