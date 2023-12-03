package com.motivey.controller;

import com.motivey.dto.AbilityActivationRequest;
import com.motivey.model.AbilityEffect;
import com.motivey.model.User;
import com.motivey.repository.StatRepository;
import com.motivey.repository.TaskRepository;
import com.motivey.repository.UserRepository;
import com.motivey.repository.UserTaskRepository;
import com.motivey.service.AbilitiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.attribute.UserPrincipal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AbilityController {

    @Autowired
    private AbilitiesManager abilitiesManager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StatRepository statRepository;

    @Autowired
    private UserTaskRepository userTaskRepository;

    @PostMapping("/activate-ability")
    public ResponseEntity<?> activateAbility(@RequestBody AbilityActivationRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUserEmail = authentication.getName();
            User user = userRepository.findByEmail(loggedInUserEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            AbilityEffect effect = new AbilityEffect(
                    request.getAbilityType(),
                    request.getEffectMagnitude(),
                    request.getDuration(),
                    LocalDateTime.now(),
                    request.getCooldown(),
                    request.getManaCost()
            );

            abilitiesManager.activateAbility(user, effect);
            userRepository.save(user); // Save the updated user

            return ResponseEntity.ok("Ability activated successfully");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatus()).body(ex.getReason());
        }

    }

    @GetMapping("/ability-cooldowns")
    public ResponseEntity<?> getAbilityCooldowns() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();
        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        LocalDateTime now = LocalDateTime.now();
        List<Map<String, Object>> cooldowns = user.getAbilityEffects().stream()
                .filter(effect -> !effect.isOffCooldown(now))
                .map(effect -> {
                    Map<String, Object> cooldownInfo = new HashMap<>();
                    cooldownInfo.put("abilityType", effect.getAbilityType());
                    cooldownInfo.put("remainingCooldown", Duration.between(now, effect.getStartTime().plus(effect.getCooldown())).toSeconds());
                    return cooldownInfo;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(cooldowns);
    }

    @DeleteMapping("/delete-user-abilities")
    public ResponseEntity<?> deleteUserAbilities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();
        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.getAbilityEffects().clear(); // Clear all ability effects
        userRepository.save(user); // Save the updated user

        return ResponseEntity.ok("All abilities deleted successfully");
    }

    @PostMapping("/apply-regeneration")
    public ResponseEntity<?> applyRegenerationEffect() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();
        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        abilitiesManager.applyEffects(user);
        return ResponseEntity.ok("Regeneration effect applied successfully");
    }

    @GetMapping("/user-abilities")
    public ResponseEntity<?> getUserAbilities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();
        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        LocalDateTime now = LocalDateTime.now();
        List<AbilityEffect> activeAbilities = user.getAbilityEffects().stream()
                .filter(effect -> effect.isActive(now))
                .collect(Collectors.toList());

        return ResponseEntity.ok(activeAbilities);
    }
}
