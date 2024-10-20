package com.clinicalnursing.userservice.controller;

import com.clinicalnursing.userservice.model.Appointment;
import com.clinicalnursing.userservice.model.AppointmentStatus;
import com.clinicalnursing.userservice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    public void setup() {
        appointmentRepository.deleteAll();

        // Add a past appointment
        Appointment pastAppointment = new Appointment(1L, 2L, LocalDateTime.now().minusDays(2), AppointmentStatus.COMPLETED);
        appointmentRepository.save(pastAppointment);

        // Add an upcoming appointment
        Appointment upcomingAppointment = new Appointment(1L, 2L, LocalDateTime.now().plusDays(5), AppointmentStatus.NEW);
        appointmentRepository.save(upcomingAppointment);
    }

    @Test
    public void testGetNewAppointments() throws Exception {
        mockMvc.perform(get("/api/appointments/new/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", is("NEW")));
    }

    @Test
    public void testGetUpcomingAppointments() throws Exception {
        mockMvc.perform(get("/api/appointments/upcoming/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].status", is("NEW")));
    }

    @Test
    public void testGetPastAppointments() throws Exception {
        mockMvc.perform(get("/api/appointments/past/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].status", is("COMPLETED")));
    }

    @Test
    public void testCreateAppointment() throws Exception {
        String newAppointmentJson = "{ \"userId\": 1, \"nurseId\": 2, \"date\": \"" + LocalDateTime.now().plusDays(1) + "\", \"status\": \"NEW\" }";

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newAppointmentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("NEW")));
    }
}
