package com.mfa.report.service;


import com.mfa.report.repository.RecommendationRepository;
import com.mfa.report.model.Recommendation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecommendationService {
    private final RecommendationRepository repository;

    public Recommendation getRecommendationByActivity (String id){
        return  repository.findByActivityId(id);
    }

    public Recommendation crUpdateRecommendation(Recommendation recommendation){
        return repository.save(recommendation);
    }
}
