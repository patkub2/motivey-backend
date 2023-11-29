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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;

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
    public ResponseEntity<?> activateAbility(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                             @RequestBody AbilityActivationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();
        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        AbilityEffect effect = new AbilityEffect(request.getAbilityType(),
                request.getEffectMagnitude(),
                request.getDuration(),
                LocalDateTime.now());

        abilitiesManager.activateAbility(user, effect);

        return ResponseEntity.ok("Ability activated successfully");
    }
}
