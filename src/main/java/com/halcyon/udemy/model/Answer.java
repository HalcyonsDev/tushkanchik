package com.halcyon.udemy.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "answers")
@ToString
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String content;

    @NonNull
    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @JsonManagedReference
    private Question question;

    @ManyToMany
    @JoinTable(
            name = "answers_scores",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "score_id")
    )
    @JsonManagedReference
    private List<Score> scoresWhereCorrect;

    @ManyToMany
    @JoinTable(
            name = "answers_scores",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "score_id")
    )
    @JsonManagedReference
    private List<Score> scoresWhereWrong;
}
