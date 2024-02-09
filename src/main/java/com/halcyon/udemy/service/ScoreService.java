package com.halcyon.udemy.service;

import com.halcyon.udemy.model.Score;
import com.halcyon.udemy.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreService {
    private final ScoreRepository scoreRepository;

    public Score create(Score score) {
        return scoreRepository.save(score);
    }
}
