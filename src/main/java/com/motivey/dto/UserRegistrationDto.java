package com.motivey.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRegistrationDto {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Provide a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotBlank(message = "Password is required")
    private String password;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    // You might want to add logic to compare password and confirmPassword
    public boolean passwordMatches() {
        return this.password.equals(this.confirmPassword);
    }
}
