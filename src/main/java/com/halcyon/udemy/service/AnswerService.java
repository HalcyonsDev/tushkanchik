package com.halcyon.udemy.service;

import com.halcyon.udemy.dto.NewAnswerDto;
import com.halcyon.udemy.model.Answer;
import com.halcyon.udemy.model.Question;
import com.halcyon.udemy.model.Score;
import com.halcyon.udemy.model.User;
import com.halcyon.udemy.repository.AnswerRepository;
import com.halcyon.udemy.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final UserService userService;
    private final AuthService authService;
    private final QuestionService questionService;
    private final ScoreService scoreService;

    public Answer create(NewAnswerDto dto) {
        Question question = questionService.getQuestion(dto.getQuestionId());

        Answer answer = new Answer(dto.getContent(), dto.isCorrect());
        answer.setQuestion(question);

        return answerRepository.save(answer);
    }

    public Answer getAnswer(Long id) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer with this id not found."));

        if (!answer.getQuestion().getTest().getCourse().getUsers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this course.");
        }

        return answer;
    }

    public Score solve(List<Answer> answers) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        Score score = new Score();
        score.setWrongAnswers(new ArrayList<>());
        score.setWrongAnswers(new ArrayList<>());

        int scoreCount = 0;
        for (Answer answer: answers) {
            if (answer.getIsCorrect()) {
                score.getCorrectAnswers().add(answer);
                scoreCount++;
                answer.getScoresWhereCorrect().add(score);
            } else {
                score.getWrongAnswers().add(answer);
                answer.getScoresWhereWrong().add(score);
            }
        }

        score.setScore(scoreCount);
        score.setUser(user);

        answerRepository.saveAll(answers);

        return scoreService.create(score);
    }
}
