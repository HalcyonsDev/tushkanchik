package com.halcyon.udemy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "scores")
@ToString
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private int score;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @JsonManagedReference
    private Question question;

    @ManyToMany(mappedBy = "scoresWhereCorrect")
    @JsonBackReference
    private List<Answer> correctAnswers;

    @ManyToMany(mappedBy = "scoresWhereWrong")
    @JsonBackReference
    private List<Answer> wrongAnswers;
}
