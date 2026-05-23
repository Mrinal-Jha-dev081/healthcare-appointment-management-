package com.healthcare.service;

import com.healthcare.dto.AppointmentRequest;
import com.healthcare.dto.AppointmentResponse;
import com.healthcare.model.*;
import com.healthcare.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final AppointmentLogRepository logRepository;
    private final EventPublisherService eventPublisherService;

    @Transactional
    public AppointmentResponse createAppointment(String userEmail, AppointmentRequest request) {
        User patient = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        if (!slot.isAvailable()) {
            throw new IllegalStateException("Slot is no longer available");
        }

        // Check for existing booking on this slot (not cancelled)
        if (appointmentRepository.existsBySlotIdAndStatusNot(slot.getId(), AppointmentStatus.CANCELLED)) {
            throw new IllegalStateException("Slot is already booked");
        }

        // Mark slot as unavailable
        slot.setAvailable(false);
        slotRepository.save(slot);

        // Create appointment
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(slot.getDoctor())
                .slot(slot)
                .status(AppointmentStatus.CONFIRMED)
                .build();

        appointment = appointmentRepository.save(appointment);

        // Log status change
        logStatusChange(appointment, null, AppointmentStatus.CONFIRMED, "user");

        // Publish event to RabbitMQ
        eventPublisherService.publishAppointmentCreated(appointment);

        return toResponse(appointment);
    }

    @Transactional
    public void cancelAppointment(String userEmail, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (!appointment.getPatient().getEmail().equals(userEmail)) {
            throw new SecurityException("You can only cancel your own appointments");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Appointment is already cancelled");
        }

        AppointmentStatus oldStatus = appointment.getStatus();
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // Free up the slot
        Slot slot = appointment.getSlot();
        slot.setAvailable(true);
        slotRepository.save(slot);

        // Log
        logStatusChange(appointment, oldStatus, AppointmentStatus.CANCELLED, "user");

        // Publish cancel event
        eventPublisherService.publishAppointmentCancelled(appointment);
    }

    public List<AppointmentResponse> getUserAppointments(String userEmail) {
        User patient = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return appointmentRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void updateStatus(Long appointmentId, AppointmentStatus newStatus, String changedBy) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        AppointmentStatus oldStatus = appointment.getStatus();
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);

        logStatusChange(appointment, oldStatus, newStatus, changedBy);
    }

    private void logStatusChange(Appointment appointment, AppointmentStatus oldStatus,
                                 AppointmentStatus newStatus, String changedBy) {
        AppointmentLog log = AppointmentLog.builder()
                .appointment(appointment)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy(changedBy)
                .build();
        logRepository.save(log);
    }

    private AppointmentResponse toResponse(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .doctorName(a.getDoctor().getName())
                .specialization(a.getDoctor().getSpecialization())
                .date(a.getSlot().getDate())
                .startTime(a.getSlot().getStartTime())
                .endTime(a.getSlot().getEndTime())
                .status(a.getStatus())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
