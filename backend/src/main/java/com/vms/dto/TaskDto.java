package com.vms.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class TaskDto {
    private Long id;
    private Long eventId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Set<Long> requiredSkillIds;
}
