package com.vms.dto;

import lombok.Data;

@Data
public class CheckInRequest {
    private Long taskId;
    private String location;
    // Optional QR code payload can be added here
}
