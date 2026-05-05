package com.vms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import java.util.List;

@Entity
@Table(name = "volunteers")
@Data
public class Volunteer {
    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_hours")
    private Double totalHours = 0.0;

    private Double rating = 0.0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "volunteer_skills",
        joinColumns = @JoinColumn(name = "volunteer_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills;

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Availability> availabilities;
}
