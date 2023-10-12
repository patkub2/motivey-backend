package com.app.motivey.controller;

import com.app.motivey.dto.UserDto;
import com.app.motivey.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";  // This refers to a Thymeleaf template "list" inside a "user" directory.
    }

    @PostMapping("/create")
    public String createUser(UserDto userDto) {
        userService.createUser(userDto);
        return "redirect:/users";  // Redirect to list of users after creation
    }

    // Other endpoints like create, update, delete etc.
}