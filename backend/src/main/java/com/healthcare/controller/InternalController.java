package com.healthcare.controller;

import com.healthcare.dto.ApiResponse;
import com.healthcare.model.AppointmentStatus;
import com.healthcare.service.AppointmentService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
@Hidden
public class InternalController {

    private final AppointmentService appointmentService;

    @Value("${app.internal.api-key}")
    private String internalApiKey;

    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("X-Internal-Key") String apiKey) {

        if (!internalApiKey.equals(apiKey)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Invalid API key"));
        }

        String statusStr = body.get("status");
        if (statusStr == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Status is required"));
        }

        AppointmentStatus status = AppointmentStatus.valueOf(statusStr);
        appointmentService.updateStatus(id, status, "notification-service");

        return ResponseEntity.ok(ApiResponse.success("Status updated"));
    }
}
