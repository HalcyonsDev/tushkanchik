package com.halcyon.udemy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewAnswerDto {
    private Long questionId;
    private String content;
    private boolean isCorrect;
}
