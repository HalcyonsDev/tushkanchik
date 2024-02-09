package com.halcyon.udemy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "questions")
@ToString
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String content;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    @JsonManagedReference
    private Test test;

    @OneToMany(mappedBy = "question")
    @JsonBackReference
    private List<Answer> answers;
}
