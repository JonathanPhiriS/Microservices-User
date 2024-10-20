package com.clinicalnursing.userservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clinicalnursing.userservice.model.User;
import com.clinicalnursing.userservice.model.UserRegistrationDTO;
import com.clinicalnursing.userservice.model.UserProfileDTO;
import com.clinicalnursing.userservice.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registration method
    public String registerUser(UserRegistrationDTO registrationDTO) {
        // Check if the user already exists
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            return "Email is already registered";
        }

        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());

        // Create and save the new user
        User newUser = new User(
            registrationDTO.getFirstName(),
            registrationDTO.getLastName(),
            registrationDTO.getEmail(),
            encodedPassword,
            registrationDTO.getAddress(),
            registrationDTO.getGender(),
            registrationDTO.getAllergies(),
            registrationDTO.getNextOfKin(),
            "ROLE_USER"  // Assigning default role
        );
        userRepository.save(newUser);

        return "User registered successfully";
    }

    // Login method
    public String loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Check password using PasswordEncoder
            if (passwordEncoder.matches(password, user.getPassword())) {
                return "Login successful";
            }
        }

        return "Invalid credentials";
    }

    // Fetch user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // Update user profile method
    public String updateUserProfile(String email, UserProfileDTO updatedProfile) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update user details with new information from the profile
            user.setFirstName(updatedProfile.getFirstName());
            user.setLastName(updatedProfile.getLastName());
            user.setAddress(updatedProfile.getAddress());
            user.setGender(updatedProfile.getGender());
            user.setAllergies(updatedProfile.getAllergies());
            user.setNextOfKin(updatedProfile.getNextOfKin());

            // Save updated user to the database
            userRepository.save(user);

            return "Profile updated successfully";
        } else {
            return "User not found";
        }
    }
}
