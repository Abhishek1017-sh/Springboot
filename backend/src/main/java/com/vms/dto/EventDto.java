package com.vms.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EventDto {
    private Long id;
    private String name;
    private String location;
    private LocalDate date;
}
