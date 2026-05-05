package com.vms.dto;

import lombok.Data;

@Data
public class CheckInRequest {
    private Long volunteerId;
    private Long taskId;
    private Double latitude;
    private Double longitude;
}
