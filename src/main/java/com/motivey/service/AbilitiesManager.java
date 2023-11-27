package com.motivey.service;

import com.motivey.enums.Ability;
import com.motivey.enums.StatType;
import com.motivey.model.AbilityEffect;
import com.motivey.model.Stat;
import com.motivey.model.Task;
import com.motivey.model.User;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AbilitiesManager {

    // Method to apply the effects of abilities
    public void applyEffects(User user) {
        applyRegenerationEffects(user);
        // Other effect application methods can be added here
    }

    private void applyRegenerationEffects(User user) {
        LocalDateTime now = LocalDateTime.now();
        int manaRegenRate = calculateManaRegenerationRate(user, now);
        int hpRegenRate = calculateHpRegenerationRate(user, now);

        // Apply HP and Mana regeneration
        user.setCurrentHp(regenerateStat(user.getCurrentHp(), user.getMaxHp(), hpRegenRate, user.getLastHpUpdate(), now));
        user.setCurrentMana(regenerateStat(user.getCurrentMana(), user.getMaxMana(), manaRegenRate, user.getLastManaUpdate(), now));

        user.setLastHpUpdate(now);
        user.setLastManaUpdate(now);
    }

    private int regenerateStat(int currentStat, int maxStat, int regenRate, LocalDateTime lastUpdate, LocalDateTime now) {
        if (lastUpdate != null) {
            long hoursElapsed = ChronoUnit.HOURS.between(lastUpdate, now);
            return Math.toIntExact(Math.min(maxStat, currentStat + regenRate * hoursElapsed));
        }
        return currentStat;
    }

    private int calculateManaRegenerationRate(User user, LocalDateTime now) {
        int baseRate = 5;
        for (AbilityEffect effect : user.getAbilityEffects()) {
            if (effect.getAbilityType() == Ability.ARCANE_INSIGHT && effect.isActive(now)) {
                return baseRate + effect.getEffectMagnitude();
            }
        }
        return baseRate;
    }

    private int calculateHpRegenerationRate(User user, LocalDateTime now) {
        int baseRate = 5;
        for (AbilityEffect effect : user.getAbilityEffects()) {
            if (effect.getAbilityType() == Ability.IRON_RESOLVE && effect.isActive(now)) {
                return baseRate + effect.getEffectMagnitude();
            }
        }
        return baseRate;
    }

    public void completeTask(User user, Task task) {
        int experienceGain = task.getExperience();

        // Apply WISDOM_WAVE effect if applicable
        if (task.getType() == StatType.INT) {
            for (AbilityEffect effect : user.getAbilityEffects()) {
                if (effect.getAbilityType() == Ability.WISDOM_WAVE && effect.isActive(LocalDateTime.now())) {
                    experienceGain += experienceGain * effect.getEffectMagnitude() / 100;
                    break; // Assuming only one effect of the same type is active at a time
                }
            }
        }

        // Add experience to overall level and specific stat
        user.addExperience(experienceGain / 2); // Half of the experience to the overall level
        allocateStatExperience(user, task.getType(), experienceGain); // Full experience to the specific stat
    }

    private void allocateStatExperience(User user, StatType statType, int experience) {
        for (Stat stat : user.getStats()) {
            if (stat.getId().getType().equals(statType.toString())) {
                stat.addStatExperience(experience);
                break;
            }
        }
    }

    // Other methods as before
}
