package com.vms.dto;

import lombok.Data;
import java.util.List;

@Data
public class VolunteerProfileDto {
    private Long id;
    private String name;
    private String email;
    private Double totalHours;
    private Double rating;
    private List<SkillDto> skills;
    private List<AvailabilityDto> availabilities;
}
