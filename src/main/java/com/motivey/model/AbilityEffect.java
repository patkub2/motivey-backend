package com.motivey.model;

import javax.persistence.Embeddable;

import com.motivey.enums.AbilityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbilityEffect {
    private AbilityType abilityType;
    private int magnitude;
    private Duration duration;
    private LocalDateTime startTime;
    private Duration cooldown;
    private int manaCost;
    // Method to check if the ability effect is currently active
    public boolean isActive(LocalDateTime currentTime) {
        if (startTime == null || duration == null) {
            return false;
        }
        LocalDateTime endTime = startTime.plus(duration);
        return currentTime.isAfter(startTime) && currentTime.isBefore(endTime);
    }

    // Determine if the ability is off cooldown
    public boolean isOffCooldown(LocalDateTime currentTime) {
        if (startTime == null || cooldown == null) {
            return true; // If no start time or cooldown, consider it off cooldown
        }
        LocalDateTime cooldownEnd = startTime.plus(cooldown);
        return currentTime.isAfter(cooldownEnd) || currentTime.isEqual(cooldownEnd);
    }


    // Method to get the effect magnitude
    public int getEffectMagnitude() {
        return magnitude;
    }
}
