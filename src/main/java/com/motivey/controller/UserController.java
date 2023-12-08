package com.motivey.controller;


import com.motivey.dto.UserDto;
import com.motivey.model.User;
import com.motivey.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();

        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserDto userDTO = convertToUserDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    private UserDto convertToUserDTO(User user) {
        UserDto dto = new UserDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCharacterLevel(user.getCharacterLevel());
        dto.setMaxExp(user.getMaxExp());
        dto.setCurrentExp(user.getCurrentExp());
        dto.setMaxHp(user.getMaxHp());
        dto.setCurrentHp(user.getCurrentHp());
        dto.setMaxMana(user.getMaxMana());
        dto.setCurrentMana(user.getCurrentMana());
        dto.setCurrentArmorId(user.getCurrentArmor() != null ? user.getCurrentArmor().getArmorId() : null);

        return dto;
    }


}
