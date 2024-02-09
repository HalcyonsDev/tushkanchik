package com.halcyon.udemy.controller;

import com.halcyon.udemy.dto.NewCourseDto;
import com.halcyon.udemy.model.Course;
import com.halcyon.udemy.model.Transaction;
import com.halcyon.udemy.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Course> create(@RequestBody NewCourseDto dto) {
        Course course = courseService.create(dto);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getById(@PathVariable Long courseId) {
        Course course = courseService.getCourse(courseId);
        return ResponseEntity.ok(course);
    }

    @PostMapping("/{courseId}/buy")
    public ResponseEntity<Transaction> buy(@PathVariable Long courseId) {
        Transaction transaction = courseService.buyById(courseId);
        return ResponseEntity.ok(transaction);
    }
}
