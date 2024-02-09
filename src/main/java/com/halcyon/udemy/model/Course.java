package com.halcyon.udemy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(
        name = "courses",
        indexes = {
                @Index(name = "idx_title", columnList = "title")
        }
)
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private int price;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @JsonManagedReference
    @NonNull
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "courses_users",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    private List<User> users;

    @OneToMany(mappedBy = "course")
    @JsonBackReference
    private List<Page> pages;

    @OneToMany(mappedBy = "course")
    @JsonBackReference
    private List<Test> tests;
}
