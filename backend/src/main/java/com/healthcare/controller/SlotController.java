package com.healthcare.controller;

import com.healthcare.dto.ApiResponse;
import com.healthcare.dto.SlotResponse;
import com.healthcare.model.Doctor;
import com.healthcare.service.SlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@Tag(name = "Slots", description = "Available appointment slots")
@SecurityRequirement(name = "bearerAuth")
public class SlotController {

    private final SlotService slotService;

    @GetMapping
    @Operation(summary = "Get available slots for a doctor on a specific date")
    public ResponseEntity<ApiResponse<List<SlotResponse>>> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<SlotResponse> slots = slotService.getAvailableSlots(doctorId, date);
        return ResponseEntity.ok(ApiResponse.success("Available slots retrieved", slots));
    }

    @GetMapping("/doctors")
    @Operation(summary = "Get all doctors")
    public ResponseEntity<ApiResponse<List<Doctor>>> getDoctors() {
        List<Doctor> doctors = slotService.getAllDoctors();
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved", doctors));
    }
}
