package com.vms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;

    private Integer rating;
    private String comment;
}
