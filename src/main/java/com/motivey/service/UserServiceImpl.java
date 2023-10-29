package com.motivey.service;

import com.motivey.dto.UserRegistrationDto;
import com.motivey.exception.UserAlreadyExistsException;
import com.motivey.model.*;
import com.motivey.repository.RoleRepository;
import com.motivey.repository.StatRepository;
import com.motivey.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatRepository statRepository;
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

        user = userRepository.save(user); // Save the user to ensure it has an ID before creating stats

        // Add default stats to the user
        for (StatType type : StatType.values()) {
            Stat stat = new Stat();
            StatId statId = new StatId();
            statId.setUserId(user.getId());
            statId.setType(type.name());
            stat.setId(statId);
            stat.setUser(user);
            statRepository.save(stat); // Assuming you have StatRepository injected and available
        }

        return user;
    }

}
