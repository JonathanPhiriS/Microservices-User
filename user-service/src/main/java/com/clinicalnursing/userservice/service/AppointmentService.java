package com.clinicalnursing.userservice.service;

import com.clinicalnursing.userservice.model.Appointment;
import com.clinicalnursing.userservice.model.AppointmentStatus;
import com.clinicalnursing.userservice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Step 1: Create a new appointment
    public Appointment createAppointment(Appointment appointment) {
        // Set the status of the appointment to NEW
        appointment.setStatus(AppointmentStatus.NEW);
        // Save the appointment to the repository
        return appointmentRepository.save(appointment);
    }

    // Step 2: Get all new appointments for a specific user
    public List<Appointment> getNewAppointments(Long userId) {
        // Retrieve appointments with the status NEW for the given user ID
        return appointmentRepository.findByUserIdAndStatus(userId, AppointmentStatus.NEW);
    }

    // Step 3: Get all upcoming appointments for a specific user
    public List<Appointment> getUpcomingAppointments(Long userId) {
        // Retrieve appointments with dates after the current date and time
        return appointmentRepository.findByUserIdAndDateAfter(userId, LocalDateTime.now());
    }

    // Step 4: Get all past appointments for a specific user
    public List<Appointment> getPastAppointments(Long userId) {
        // Retrieve appointments with dates before the current date and time
        return appointmentRepository.findByUserIdAndDateBefore(userId, LocalDateTime.now());
    }
}
