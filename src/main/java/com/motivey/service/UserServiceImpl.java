package com.motivey.service;

import com.motivey.dto.UserRegistrationDto;
import com.motivey.enums.StatType;
import com.motivey.exception.UserAlreadyExistsException;
import com.motivey.model.*;
import com.motivey.repository.RoleRepository;
import com.motivey.repository.StatRepository;
import com.motivey.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.motivey.model.User.BASE_EXP;
import static com.motivey.model.User.GROWTH_RATE;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatRepository statRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ArmorService armorService;
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
    @Override
    public void addExperience(User user, int exp) {
        user.setCurrentExp(user.getCurrentExp() + exp);

        while (user.getCurrentExp() >= user.getMaxExp()) {
            // Level up
            user.setCharacterLevel(user.getCharacterLevel() + 1);
            user.setCurrentExp(user.getCurrentExp() - user.getMaxExp());

            // Update maxExp for the next level
            // Assuming BASE_EXP and GROWTH_RATE are constants defined somewhere
            user.setMaxExp((int)(BASE_EXP * Math.pow(GROWTH_RATE, user.getCharacterLevel() - 1)));

            // Additional logic for other effects of leveling up can be added here
        }

        // Save the updated user
        userRepository.save(user);
    }

}
