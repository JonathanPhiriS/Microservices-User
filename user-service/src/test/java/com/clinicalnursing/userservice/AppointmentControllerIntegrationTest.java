package com.clinicalnursing.userservice;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clinicalnursing.userservice.model.Appointment;
import com.clinicalnursing.userservice.model.AppointmentStatus;
import com.clinicalnursing.userservice.repository.AppointmentRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    public void setup() {
        appointmentRepository.deleteAll(); // Clean up the database before each test
    }

    @Test
    public void testCreateAppointment() throws Exception {
        String appointmentJson = "{ \"userId\": 1, \"nurseId\": 2, \"date\": \"2024-10-10T10:00:00\", \"status\": \"NEW\" }";

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(appointmentJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetNewAppointments() throws Exception {
        Appointment appointment = new Appointment(1L, 2L, LocalDateTime.now().plusDays(1), AppointmentStatus.NEW);
        appointmentRepository.save(appointment); // Save a sample appointment

        mockMvc.perform(get("/api/appointments/new/1")) // Ensure this endpoint matches your API
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L));
    }

    @Test
    public void testGetUpcomingAppointments() throws Exception {
        Appointment appointment = new Appointment(1L, 2L, LocalDateTime.now().plusDays(1), AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment); // Save a sample appointment

        mockMvc.perform(get("/api/appointments/upcoming/1")) // Ensure this endpoint matches your API
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L));
    }

    @Test
    public void testGetPastAppointments() throws Exception {
        Appointment appointment = new Appointment(1L, 2L, LocalDateTime.now().minusDays(1), AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment); // Save a sample appointment

        mockMvc.perform(get("/api/appointments/past/1")) // Ensure this endpoint matches your API
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L));
    }
}
