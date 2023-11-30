package com.motivey.service;

import com.motivey.enums.AbilityType;
import com.motivey.enums.StatType;
import com.motivey.model.AbilityEffect;
import com.motivey.model.Stat;
import com.motivey.model.Task;
import com.motivey.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AbilitiesManager {

    @Autowired
    private UserService userService;

    // Method to apply the effects of abilities
    public void applyEffects(User user) {
        applyRegenerationEffects(user);
        // Other effect application methods can be added here
    }

    public void activateAbility(User user, AbilityEffect effect) {
        // Add logic to activate the ability
        user.getAbilityEffects().add(effect);
        // Save user or perform necessary updates
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
            if (effect.getAbilityType() == AbilityType.ARCANE_INSIGHT && effect.isActive(now)) {
                return baseRate + effect.getEffectMagnitude();
            }
        }
        return baseRate;
    }

    private int calculateHpRegenerationRate(User user, LocalDateTime now) {
        int baseRate = 5;
        for (AbilityEffect effect : user.getAbilityEffects()) {
            if (effect.getAbilityType() == AbilityType.IRON_RESOLVE && effect.isActive(now)) {
                return baseRate + effect.getEffectMagnitude();
            }
        }
        return baseRate;
    }

    public void applySteadfastAbility(User user) {
        final int healingAmount = 50; // Amount of HP to heal
        user.setCurrentHp(Math.min(user.getCurrentHp() + healingAmount, user.getMaxHp()));
    }

    public void applyMindSurgeAbility(User user) {
        int manaRestoreAmount = 50; // Amount to restore mana
        int newMana = Math.min(user.getCurrentMana() + manaRestoreAmount, user.getMaxMana());
        user.setCurrentMana(newMana);
    }
    public void completeTask(User user, Task task) {
        int experienceGain = task.getExperience();

        // Apply WISDOM_WAVE effect if the task type is INT
        if (task.getType() == StatType.INT) {
            experienceGain = applyAbilityEffect(user, task, experienceGain, AbilityType.WISDOM_WAVE);
        }
        // Apply NIMBLE_MIND effect if the task type is AGI
        else if (task.getType() == StatType.AGI) {
            experienceGain = applyAbilityEffect(user, task, experienceGain, AbilityType.NIMBLE_MIND);
        }
        // Apply NIMBLE_MIND effect if the task type is AGI
        else if (task.getType() == StatType.STR) {
            experienceGain = applyAbilityEffect(user, task, experienceGain, AbilityType.TITAN_S_GRIP);
        }
        // Apply NIMBLE_MIND effect if the task type is AGI
        else if (task.getType() == StatType.VIT) {
            experienceGain = applyAbilityEffect(user, task, experienceGain, AbilityType.LIFE_S_BOUNTY);
        }

        // Add experience to overall level and specific stat
        userService.addExperience(user,experienceGain / 2); // Half of the experience to the overall level
        allocateStatExperience(user, task.getType(), experienceGain); // Full experience to the specific stat
    }

    private int applyAbilityEffect(User user, Task task, int baseExperience, AbilityType abilityType) {
        for (AbilityEffect effect : user.getAbilityEffects()) {
            if (effect.getAbilityType() == abilityType && effect.isActive(LocalDateTime.now())) {
                return baseExperience + baseExperience * effect.getEffectMagnitude() / 100;
            }
        }
        return baseExperience;
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
