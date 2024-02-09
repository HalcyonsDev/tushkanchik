package com.halcyon.udemy.controller;

import com.halcyon.udemy.dto.NewTestDto;
import com.halcyon.udemy.model.Test;
import com.halcyon.udemy.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @PostMapping
    public ResponseEntity<Test> create(@RequestBody NewTestDto dto) {
        Test test = testService.create(dto);
        return ResponseEntity.ok(test);
    }

    @GetMapping("/{testId}")
    public ResponseEntity<Test> getById(@PathVariable Long testId) {
        Test test = testService.getTest(testId);
        return ResponseEntity.ok(test);
    }
}
