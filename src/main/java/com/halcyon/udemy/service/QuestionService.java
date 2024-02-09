package com.halcyon.udemy.service;

import com.halcyon.udemy.dto.NewQuestionDto;
import com.halcyon.udemy.model.Question;
import com.halcyon.udemy.model.Test;
import com.halcyon.udemy.model.User;
import com.halcyon.udemy.repository.QuestionRepository;
import com.halcyon.udemy.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final AuthService authService;
    private final TestService testService;

    public Question create(NewQuestionDto dto) {
        Test test = testService.getTest(dto.getTestId());

        Question question = new Question(dto.getContent());
        question.setTest(test);

        return questionRepository.save(question);
    }

    public Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question with this id not found."));
    }

    public Question getQuestion(Long id) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Question question = findById(id);

        if (!question.getTest().getCourse().getUsers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this course.");
        }

        return question;
    }
}
