package com.healthcare.repository;

import com.healthcare.model.Appointment;
import com.healthcare.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    boolean existsBySlotIdAndStatusNot(Long slotId, AppointmentStatus status);
}
