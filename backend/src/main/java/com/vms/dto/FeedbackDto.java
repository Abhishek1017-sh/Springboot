package com.vms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeedbackDto {
    private Long id;
    private Long reviewerId;
    private Long targetId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
