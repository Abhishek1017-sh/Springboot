package com.vms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "volunteers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"skills", "badges"})
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double totalHours = 0.0;
    private Double rating = 0.0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "volunteer_skills",
        joinColumns = @JoinColumn(name = "volunteer_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "volunteer_badges",
        joinColumns = @JoinColumn(name = "volunteer_id"),
        inverseJoinColumns = @JoinColumn(name = "badge_id")
    )
    private Set<Badge> badges = new HashSet<>();
}
