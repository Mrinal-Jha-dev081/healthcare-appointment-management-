package com.healthcare.controller;

import com.healthcare.dto.*;
import com.healthcare.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointment booking and management")
@SecurityRequirement(name = "bearerAuth")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Book a new appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            Authentication auth,
            @Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.createAppointment(auth.getName(), request);
        return ResponseEntity.ok(ApiResponse.success("Appointment booked successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel an appointment")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(
            Authentication auth,
            @PathVariable Long id) {
        appointmentService.cancelAppointment(auth.getName(), id);
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all appointments for the logged-in user")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyAppointments(Authentication auth) {
        List<AppointmentResponse> appointments = appointmentService.getUserAppointments(auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved", appointments));
    }
}
