package com.motivey.service;

import com.motivey.dto.UserRegistrationDto;
import com.motivey.exception.UserAlreadyExistsException;
import com.motivey.model.Role;
import com.motivey.model.User;
import com.motivey.repository.RoleRepository;
import com.motivey.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User register(UserRegistrationDto registrationDto) {
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("A user with email " + registrationDto.getEmail() + " already exists");
        }

        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        // Set a default role (e.g., "ROLE_USER") for the new user
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("User role not found"));
        user.setRole(userRole);

        return userRepository.save(user);
    }
}
