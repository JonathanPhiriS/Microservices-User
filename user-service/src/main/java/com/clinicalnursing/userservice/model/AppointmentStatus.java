package com.clinicalnursing.userservice.model;

public enum AppointmentStatus {
    NEW,           // Appointment is newly created and awaiting confirmation
    CONFIRMED,     // Appointment has been confirmed by the nurse or system
    IN_PROGRESS,   // Appointment is currently in progress
    COMPLETED,     // Appointment has been completed
    CANCELLED      // Appointment has been cancelled by user or nurse
}
