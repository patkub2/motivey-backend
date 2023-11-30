package com.motivey.service;

import com.motivey.dto.UserRegistrationDto;
import com.motivey.model.User;

public interface UserService {
    User register(UserRegistrationDto registrationDto);

    void addExperience(User user, int exp);
    // other method declarations
}
