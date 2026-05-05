package com.vms.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskCreationDto {
    private Long eventId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<String> requiredSkills;
}
