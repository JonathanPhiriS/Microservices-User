package com.clinicalnursing.userservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.clinicalnursing.userservice.model.Appointment;
import com.clinicalnursing.userservice.model.User;
import com.clinicalnursing.userservice.model.UserProfileDTO;
import com.clinicalnursing.userservice.model.UserRegistrationDTO;
import com.clinicalnursing.userservice.service.UserService;
import com.clinicalnursing.userservice.service.AppointmentService; // Ensure you import the AppointmentService

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService; // Add the AppointmentService here

    // Handle POST request to register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO, BindingResult result) {
        // Handle validation errors
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        // Call the service to register the user
        String registerResult = userService.registerUser(registrationDTO);

        // Handle if the email is already registered
        if (registerResult.equals("Email is already registered")) {
            return new ResponseEntity<>(registerResult, HttpStatus.CONFLICT);
        }

        // If registration is successful, return a success message
        return new ResponseEntity<>(registerResult, HttpStatus.CREATED);
    }

    // Handle POST request for user login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        // Validate the user using the service
        String loginResult = userService.loginUser(email, password);

        // Check if login is successful or credentials are invalid
        if (loginResult.equals("Invalid credentials")) {
            return new ResponseEntity<>(loginResult, HttpStatus.UNAUTHORIZED);
        }

        // Return success if login is successful
        return new ResponseEntity<>(loginResult, HttpStatus.OK);
    }

    // Handle GET request to fetch user details by email
    @GetMapping("/details/{email}")
    public ResponseEntity<?> getUserDetails(@PathVariable String email) {
        User user = userService.getUserByEmail(email);

        // Check if the user exists
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Return the user details
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Handle PUT request for updating user profile
    @PutMapping("/profile/{email}")
    public ResponseEntity<?> updateUserProfile(@PathVariable String email, @Valid @RequestBody UserProfileDTO userProfileDTO) {
        // Call the service to update the user profile
        String updateResult = userService.updateUserProfile(email, userProfileDTO);

        // Return the appropriate response
        if (updateResult.equals("User not found")) {
            return new ResponseEntity<>(updateResult, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updateResult, HttpStatus.OK);
    }

    // Handle GET request for past appointments
    @GetMapping("/appointments/past/{userId}")
    public ResponseEntity<List<Appointment>> getPastAppointments(@PathVariable Long userId) {
        List<Appointment> pastAppointments = appointmentService.getPastAppointments(userId);
        return new ResponseEntity<>(pastAppointments, HttpStatus.OK);
    }

    // Handle GET request for upcoming appointments
    @GetMapping("/appointments/upcoming/{userId}")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments(@PathVariable Long userId) {
        List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointments(userId);
        return new ResponseEntity<>(upcomingAppointments, HttpStatus.OK);
    }

    // Handle GET request for new appointments
    @GetMapping("/appointments/new/{userId}")
    public ResponseEntity<List<Appointment>> getNewAppointments(@PathVariable Long userId) {
        List<Appointment> newAppointments = appointmentService.getNewAppointments(userId);
        return new ResponseEntity<>(newAppointments, HttpStatus.OK);
    }
}
