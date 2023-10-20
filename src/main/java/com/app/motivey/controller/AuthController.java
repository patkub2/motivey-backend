package com.app.motivey.controller;

import com.app.motivey.dto.LoginDto;
import com.app.motivey.dto.UserRegistrationDto;
import com.app.motivey.dto.response.JwtResponse;
import com.app.motivey.entity.User;
import com.app.motivey.repository.UserRepository;
import com.app.motivey.service.UserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;


@RestController
@RequestMapping("/api")
public class AuthController {
  //  @Autowired
  //  private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDto registrationDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Return a list of validation error messages
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        // Check if email already exists
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }



        // Encode the password and save the user
        User newUser = new User();
        newUser.setName(registrationDto.getName());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        // You should also set the default role for the new user here, depending on your setup

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDto loginDto) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
          //  String jwt = jwtTokenUtil.generateToken(userDetails); // Assuming jwtTokenUtil is a bean you have to create JWTs

            // Return JWT
           // return ResponseEntity.ok(new JwtResponse(jwt));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}