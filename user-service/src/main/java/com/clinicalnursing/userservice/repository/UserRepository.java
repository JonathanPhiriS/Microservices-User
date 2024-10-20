package com.clinicalnursing.userservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.clinicalnursing.userservice.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);  // Custom query to find a user by email
}
