package com.halcyon.udemy.controller;

import com.halcyon.udemy.dto.NewAnswerDto;
import com.halcyon.udemy.model.Answer;
import com.halcyon.udemy.model.Score;
import com.halcyon.udemy.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<Answer> create(@RequestBody NewAnswerDto dto) {
        Answer answer = answerService.create(dto);
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/{answerId}")
    public ResponseEntity<Answer> getById(@PathVariable Long answerId) {
        Answer answer = answerService.getAnswer(answerId);
        return ResponseEntity.ok(answer);
    }

    @PostMapping("/solve")
    public ResponseEntity<Score> solve(@RequestBody List<Answer> answers) {
        Score score = answerService.solve(answers);
        return ResponseEntity.ok(score);
    }
}
