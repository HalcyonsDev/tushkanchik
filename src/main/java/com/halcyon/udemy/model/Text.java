package com.halcyon.udemy.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "texts")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Instant createdAt;

    @NonNull
    private String content;

    @Column(name = "content_order_id")
    private Long contentOrderId;

    @ManyToOne
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    @JsonManagedReference
    @NonNull
    private Page page;

    @PrePersist
    private void onCreate() {
        createdAt = Instant.now();
    }
}
