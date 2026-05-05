package com.vms.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class AvailabilityDto {
    private Long id;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
