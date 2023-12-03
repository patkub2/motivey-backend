package com.motivey.dto;

import com.motivey.enums.AbilityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbilityCooldownDto {
    private AbilityType abilityType;
    private Duration remainingCooldown;

    // Constructor, getters and setters
}
