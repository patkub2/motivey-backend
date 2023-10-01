package com.app.motivey.controller;

import com.app.motivey.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";  // This refers to a Thymeleaf template "list" inside a "user" directory.
    }

    // Other endpoints like create, update, delete etc.
}