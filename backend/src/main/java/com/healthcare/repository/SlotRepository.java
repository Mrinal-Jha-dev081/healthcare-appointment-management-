package com.healthcare.repository;

import com.healthcare.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByDoctorIdAndDateAndAvailableTrue(Long doctorId, LocalDate date);
    List<Slot> findByDoctorIdAndDate(Long doctorId, LocalDate date);
}
