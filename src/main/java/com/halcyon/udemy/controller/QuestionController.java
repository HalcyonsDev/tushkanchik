package com.halcyon.udemy.controller;

import com.halcyon.udemy.dto.NewQuestionDto;
import com.halcyon.udemy.model.Question;
import com.halcyon.udemy.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<Question> create(@RequestBody NewQuestionDto dto) {
        Question question = questionService.create(dto);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<Question> getById(@PathVariable Long questionId) {
        Question question = questionService.getQuestion(questionId);
        return ResponseEntity.ok(question);
    }
}
