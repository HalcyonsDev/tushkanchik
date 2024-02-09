package com.halcyon.udemy.service;

import com.halcyon.udemy.dto.NewTestDto;
import com.halcyon.udemy.model.Course;
import com.halcyon.udemy.model.Test;
import com.halcyon.udemy.model.User;
import com.halcyon.udemy.repository.TestRepository;
import com.halcyon.udemy.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final UserService userService;
    private final AuthService authService;
    private final CourseService courseService;

    public Test create(NewTestDto dto) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Course course = courseService.findById(dto.getCourseId());

        if (!course.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this course.");
        }

        Test test = new Test(dto.getTitle());
        test.setCourse(course);

        return testRepository.save(test);
    }

    public Test findById(Long id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Test with this id not found."));
    }

    public Test getTest(Long id) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Test test = findById(id);

        if (!test.getCourse().getUsers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this course.");
        }

        return test;
    }
}
