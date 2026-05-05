package com.vms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VolunteerRecommendationDto {
    private Long taskId;
    private String taskTitle;
    private Long eventId;
    private Double matchScore; // Percentage
}
