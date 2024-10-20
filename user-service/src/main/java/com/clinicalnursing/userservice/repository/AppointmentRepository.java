package com.clinicalnursing.userservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clinicalnursing.userservice.model.Appointment;
import com.clinicalnursing.userservice.model.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.userId = :userId AND a.status = :status")
    List<Appointment> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.userId = :userId AND a.date > :date")
    List<Appointment> findByUserIdAndDateAfter(@Param("userId") Long userId, @Param("date") LocalDateTime date);

    @Query("SELECT a FROM Appointment a WHERE a.userId = :userId AND a.date < :date")
    List<Appointment> findByUserIdAndDateBefore(@Param("userId") Long userId, @Param("date") LocalDateTime date);
}
