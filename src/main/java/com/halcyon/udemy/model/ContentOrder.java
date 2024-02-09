package com.halcyon.udemy.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "content_order")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class ContentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_id")
    @NonNull
    private Long pageId;

    @Column(name = "content_id")
    @NonNull
    private Long contentId;

    @Column(name = "content_type")
    @NonNull
    private String contentType;

    @Column(name = "content_order")
    @NonNull
    private Integer order;
}
