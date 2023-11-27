package com.motivey;

import com.motivey.enums.Ability;
import com.motivey.enums.StatType;
import com.motivey.model.*;
import com.motivey.service.AbilitiesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    private User user;

    @Mock
    private AbilitiesManager abilitiesManager;

    @BeforeEach
    public void setup() {
        abilitiesManager = new AbilitiesManager();
        user = new User();
        // other initializations if needed
    }
    @Test
    public void testArcaneInsightEffect() {
        // Setup
        AbilityEffect arcaneInsight = new AbilityEffect(Ability.ARCANE_INSIGHT, 10, Duration.ofHours(6), LocalDateTime.now()); // 10 additional mana per hour
        user.setAbilityEffects(Collections.singletonList(arcaneInsight));
        user.setCurrentMana(50);
        user.setMaxMana(100);
        user.setLastManaUpdate(LocalDateTime.now().minusHours(1)); // Last updated an hour ago

        // Invoke the method
        abilitiesManager.applyEffects(user);

        // Assertions
        assertEquals(65, user.getCurrentMana());
    }

    @Test
    public void testWisdomWaveEffect() {
        // Setup
        Stat intStat = new Stat(new StatId(user.getId(), "INT"), user, 1, 0, 1000);
        user.getStats().add(intStat); // Ensure the user has an INT stat
        Task task = new Task();
        task.setType(StatType.INT);
        task.setExperience(100); // Base experience
        AbilityEffect wisdomWave = new AbilityEffect(Ability.WISDOM_WAVE, 50, Duration.ofHours(6), LocalDateTime.now());
        user.setAbilityEffects(Collections.singletonList(wisdomWave));

        // Act
        abilitiesManager.completeTask(user, task);

        // Assert
        assertEquals(150, intStat.getCurrentExp()); // Full boosted experience to INT stat
        assertEquals(75, user.getCurrentExp()); // Half of boosted experience to overall user level
    }
    @Test
    public void testIronResolveEffect() {
        // Setup
        AbilityEffect ironResolve = new AbilityEffect(Ability.IRON_RESOLVE, 10, Duration.ofHours(6), LocalDateTime.now()); // 10 additional HP per hour
        user.setAbilityEffects(Collections.singletonList(ironResolve));
        user.setCurrentHp(50);
        user.setMaxHp(100);
        user.setLastHpUpdate(LocalDateTime.now().minusHours(1)); // Last updated an hour ago

        // Invoke the method
        abilitiesManager.applyEffects(user);

        // Assertions
        // Base regen is 5 HP per hour, plus 10 from IRON_RESOLVE, so expect 65 HP after an hour
        assertEquals(65, user.getCurrentHp());
    }

    // Other tests as needed for different abilities and scenarios
}
