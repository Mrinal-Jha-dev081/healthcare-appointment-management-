package com.healthcare.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentRequest {
    @NotNull(message = "Slot ID is required")
    private Long slotId;
}
