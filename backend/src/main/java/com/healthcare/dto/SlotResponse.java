package com.healthcare.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class SlotResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean available;
}
