package com.clinicalnursing.userservice.controller;

import com.clinicalnursing.userservice.model.Appointment;
import com.clinicalnursing.userservice.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Endpoint to create a new appointment
    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        Appointment createdAppointment = appointmentService.createAppointment(appointment);
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    // Endpoint to get all new appointments for a user
    @GetMapping("/new/{userId}")
    public ResponseEntity<List<Appointment>> getNewAppointments(@PathVariable Long userId) {
        List<Appointment> newAppointments = appointmentService.getNewAppointments(userId);
        return new ResponseEntity<>(newAppointments, HttpStatus.OK);
    }

    // Endpoint to get all upcoming appointments for a user
    @GetMapping("/upcoming/{userId}")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments(@PathVariable Long userId) {
        List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointments(userId);
        return new ResponseEntity<>(upcomingAppointments, HttpStatus.OK);
    }

    // Endpoint to get all past appointments for a user
    @GetMapping("/past/{userId}")
    public ResponseEntity<List<Appointment>> getPastAppointments(@PathVariable Long userId) {
        List<Appointment> pastAppointments = appointmentService.getPastAppointments(userId);
        return new ResponseEntity<>(pastAppointments, HttpStatus.OK);
    }
}
