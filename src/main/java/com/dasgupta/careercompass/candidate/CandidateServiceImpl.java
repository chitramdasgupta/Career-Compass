package com.dasgupta.careercompass.candidate;

import com.dasgupta.careercompass.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {
    private static final Logger log = LoggerFactory.getLogger(CandidateServiceImpl.class);

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;

    public CandidateServiceImpl(CandidateRepository candidateRepository, CandidateMapper candidateMapper) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
    }

    @Override
    public CandidateDto getCandidateByUserId(Integer userId) {
        log.info("Searching for candidate by user id: {}", userId);

        return candidateRepository.findByUserId(userId)
                .map(candidate -> {
                    log.info("Found candidate: {}", candidate);

                    return candidateMapper.toDto(candidate);
                })
                .orElseThrow(() -> {
                    log.info("No candidate found for user id: {}", userId);

                    return new EntityNotFoundException("No candidate found for user id: " + userId);
                });
    }

    @Override
    public CandidateDto createCandidate(User user) {
        log.info("Attempting to create a candidate for user: {}", user.getEmail());

        Candidate candidate = new Candidate()
                .setUser(user);

        Candidate savedCandidate = candidateRepository.save(candidate);

        log.info("Candidate created: {}", savedCandidate);
        return candidateMapper.toDto(savedCandidate);
    }
}
