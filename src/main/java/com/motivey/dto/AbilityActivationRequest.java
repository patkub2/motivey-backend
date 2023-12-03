package com.motivey.dto;

import com.motivey.enums.AbilityType;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class AbilityActivationRequest {
    private AbilityType abilityType;
    private int effectMagnitude; // Optional, based on ability
    private Duration duration;
    private Duration cooldown;
    private int manaCost;

}
