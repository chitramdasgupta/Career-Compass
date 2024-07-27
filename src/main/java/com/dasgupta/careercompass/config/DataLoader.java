package com.dasgupta.careercompass.config;

import com.dasgupta.careercompass.Company.Company;
import com.dasgupta.careercompass.Company.CompanyRepository;
import com.dasgupta.careercompass.Job.Job;
import com.dasgupta.careercompass.Job.JobRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {
    private static final int NUM_COMPANIES = 100;
    private static final int NUM_JOBS_PER_COMPANY = 25;

    private final Faker faker = new Faker();

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    @Autowired
    public DataLoader(CompanyRepository companyRepository, JobRepository jobRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
    }

    @Bean
    public CommandLineRunner loadData() {
        return (args) -> {
            loadCompanies();
            loadJobs();
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
                    job.setDescription(faker.job().field() + " - " + faker.job().keySkills());
                    job.setCompany(company);

                    jobRepository.save(job);
                }
            }
        }
    }
}
