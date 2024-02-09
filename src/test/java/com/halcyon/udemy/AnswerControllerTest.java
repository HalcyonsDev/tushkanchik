package com.halcyon.udemy;

import com.halcyon.udemy.controller.AnswerController;
import com.halcyon.udemy.model.Answer;
import com.halcyon.udemy.model.Score;
import com.halcyon.udemy.service.AnswerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnswerControllerTest {
    @Mock
    private AnswerService answerService;

    @InjectMocks
    private AnswerController answerController;

    @Test
    void solve_ReturnsListOfAnswers() {
        Answer wrongAnswer = new Answer("answer1", false);
        Answer correctAnswer = new Answer("answer2", true);

        List<Answer> answers = List.of(
                wrongAnswer,
                correctAnswer
        );

        Score score = new Score(1);
        score.setCorrectAnswers(List.of(correctAnswer));
        score.setWrongAnswers(List.of(wrongAnswer));

        when(answerService.solve(anyList())).thenReturn(score);

        ResponseEntity<Score> response = answerController.solve(answers);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(score, response.getBody());

        score = response.getBody();
        System.out.println(score);

        for (Answer answer : answers) {
            System.out.println(answer);
            if (answer.getIsCorrect()) {
                assertTrue(answer.getScoresWhereCorrect().contains(score));
            } else {
                assertTrue(answer.getScoresWhereWrong().contains(score));
            }
        }
    }
}
