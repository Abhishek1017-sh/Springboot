package com.vms.dto;

import com.vms.entity.ApplicationStatus;
import lombok.Data;

@Data
public class ApplicationDto {
    private Long id;
    private Long volunteerId;
    private Long taskId;
    private ApplicationStatus status;
}
