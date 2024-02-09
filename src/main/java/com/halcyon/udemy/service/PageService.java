package com.halcyon.udemy.service;

import com.halcyon.udemy.dto.NewPageDto;
import com.halcyon.udemy.model.Course;
import com.halcyon.udemy.model.Page;
import com.halcyon.udemy.model.User;
import com.halcyon.udemy.repository.PageRepository;
import com.halcyon.udemy.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PageService {
    private final PageRepository pageRepository;
    private final UserService userService;
    private final AuthService authService;
    private final CourseService courseService;

    public Page create(NewPageDto dto) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Course course = courseService.findById(dto.getCourseId());

        if (!course.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this course.");
        }

        Page page = new Page(dto.getName(), course);
        return pageRepository.save(page);
    }

    public Page getPage(Long id) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Page with this id not found."));

        if (!page.getCourse().getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this course.");
        }

        return page;
    }
}
