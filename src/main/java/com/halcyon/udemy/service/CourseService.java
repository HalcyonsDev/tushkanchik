package com.halcyon.udemy.service;

import com.halcyon.udemy.dto.NewCourseDto;
import com.halcyon.udemy.model.Course;
import com.halcyon.udemy.model.User;
import com.halcyon.udemy.repository.CourseRepository;
import com.halcyon.udemy.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final AuthService authService;
    private final UserService userService;

    public Course create(NewCourseDto dto) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Course course = new Course(dto.getTitle(), dto.getPrice(), user);
        course.setUsers(List.of(user));

        user.getCourses().add(course);
        userService.create(user);

        return courseRepository.save(course);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with this id not found."));
    }


    public Course getCourse(Long id) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with this id not found."));

        if (!course.getUsers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this course.");
        }

        return course;
    }
}
