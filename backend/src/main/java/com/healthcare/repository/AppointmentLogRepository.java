package com.healthcare.repository;

import com.healthcare.model.AppointmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentLogRepository extends JpaRepository<AppointmentLog, Long> {
    List<AppointmentLog> findByAppointmentIdOrderByChangedAtAsc(Long appointmentId);
}
