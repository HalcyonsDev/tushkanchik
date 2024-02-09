package com.halcyon.udemy.service;

import com.halcyon.udemy.dto.NewCourseDto;
import com.halcyon.udemy.model.Course;
import com.halcyon.udemy.model.Transaction;
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
    private final TransactionService transactionService;

    public Course create(NewCourseDto dto) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Course course = new Course(dto.getTitle(), dto.getPrice(), user);
        course.setUsers(List.of(user));

        user.getCourses().add(course);
        userService.create(user);

        return courseRepository.save(course);
    }

    public void save(Course course) {
        courseRepository.save(course);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with this id not found."));
    }

    public Course findByName(String courseTitle) {
        return courseRepository.findByTitle(courseTitle);
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

    public Transaction buyById(Long courseId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Course course = findById(courseId);

        if (user.getCourses().contains(course) || course.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The course has already been purchased by the user.");
        }

        BigDecimal userBalance = user.getBalance();
        BigDecimal amount = BigDecimal.valueOf(course.getPrice());

        if (userBalance.compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not contain sufficient funds.");
        }

        userBalance = userBalance.subtract(amount);
        user.setBalance(userBalance);

        Transaction transaction = Transaction.builder()
                .transactionDate(Calendar.getInstance().getTime())
                .transactionAmount(amount)
                .balance(userBalance)
                .user(user)
                .course(course)
                .build();

        user.getTransactions().add(transaction);
        transactionService.create(transaction);
        userService.create(user);

        course.getUsers().add(user);
        save(course);

        return transaction;
    }
}
