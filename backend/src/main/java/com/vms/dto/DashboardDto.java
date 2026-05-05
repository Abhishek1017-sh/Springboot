package com.vms.dto;

import lombok.Data;
import java.util.List;

@Data
public class DashboardDto {
    private Double totalHoursWorked;
    private Double averageRating;
    private List<String> badges; // Placeholder for badges logic
}
