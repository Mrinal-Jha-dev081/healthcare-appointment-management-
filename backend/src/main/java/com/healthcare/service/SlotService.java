package com.healthcare.service;

import com.healthcare.dto.SlotResponse;
import com.healthcare.model.Doctor;
import com.healthcare.model.Slot;
import com.healthcare.repository.DoctorRepository;
import com.healthcare.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;
    private final DoctorRepository doctorRepository;

    public List<SlotResponse> getAvailableSlots(Long doctorId, LocalDate date) {
        List<Slot> slots = slotRepository.findByDoctorIdAndDateAndAvailableTrue(doctorId, date);
        return slots.stream().map(this::toResponse).toList();
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    private SlotResponse toResponse(Slot slot) {
        return SlotResponse.builder()
                .id(slot.getId())
                .doctorId(slot.getDoctor().getId())
                .doctorName(slot.getDoctor().getName())
                .specialization(slot.getDoctor().getSpecialization())
                .date(slot.getDate())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .available(slot.isAvailable())
                .build();
    }
}
