package com.motivey;

import com.motivey.enums.AbilityType;
import com.motivey.enums.StatType;
import com.motivey.model.*;
import com.motivey.service.AbilitiesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

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
        AbilityEffect arcaneInsight = new AbilityEffect(AbilityType.ARCANE_INSIGHT, 10, Duration.ofHours(6), LocalDateTime.now()); // 10 additional mana per hour
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
    public void testIronResolveEffect() {
        // Setup
        AbilityEffect ironResolve = new AbilityEffect(AbilityType.IRON_RESOLVE, 10, Duration.ofHours(6), LocalDateTime.now()); // 10 additional HP per hour
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
    @Test
    public void testWisdomWaveEffect() {
        // Setup
        Stat intStat = new Stat(new StatId(user.getId(), "INT"), user, 1, 0, 1000);
        user.getStats().add(intStat); // Ensure the user has an INT stat
        Task task = new Task();
        task.setType(StatType.INT);
        task.setExperience(100); // Base experience
        AbilityEffect wisdomWave = new AbilityEffect(AbilityType.WISDOM_WAVE, 50, Duration.ofHours(6), LocalDateTime.now());
        user.setAbilityEffects(Collections.singletonList(wisdomWave));

        // Act
        abilitiesManager.completeTask(user, task);

        // Assert
        assertEquals(150, intStat.getCurrentExp()); // Full boosted experience to INT stat
        assertEquals(75, user.getCurrentExp()); // Half of boosted experience to overall user level
    }

    @Test
    public void testNimbleMindEffect() {
        // Setup
        Stat agiStat = new Stat(new StatId(user.getId(), "AGI"), user, 1, 0, 1000);
        user.getStats().add(agiStat); // Ensure the user has an AGI stat
        Task task = new Task();
        task.setType(StatType.AGI);
        task.setExperience(100); // Base experience
        AbilityEffect NimbleMind = new AbilityEffect(AbilityType.NIMBLE_MIND, 50, Duration.ofHours(6), LocalDateTime.now());
        user.setAbilityEffects(Collections.singletonList(NimbleMind));

        // Act
        abilitiesManager.completeTask(user, task);

        // Assert
        assertEquals(150, agiStat.getCurrentExp()); // Full boosted experience to INT stat
        assertEquals(75, user.getCurrentExp()); // Half of boosted experience to overall user level
    }

    @Test
    public void testTitanSGripEffect() {
        // Setup TITAN_S_GRIP
        Stat strStat = new Stat(new StatId(user.getId(), "STR"), user, 1, 0, 1000);
        user.getStats().add(strStat); // Ensure the user has an STR stat
        Task task = new Task();
        task.setType(StatType.STR);
        task.setExperience(100); // Base experience
        AbilityEffect TitansGrip = new AbilityEffect(AbilityType.TITAN_S_GRIP, 50, Duration.ofHours(6), LocalDateTime.now());
        user.setAbilityEffects(Collections.singletonList(TitansGrip));

        // Act
        abilitiesManager.completeTask(user, task);

        // Assert
        assertEquals(150, strStat.getCurrentExp()); // Full boosted experience to INT stat
        assertEquals(75, user.getCurrentExp()); // Half of boosted experience to overall user level
    }

    @Test
    public void testLifeSBountyEffect() {
        // Setup LIFE_S_BOUNTY
        Stat vitStat = new Stat(new StatId(user.getId(), "VIT"), user, 1, 0, 1000);
        user.getStats().add(vitStat); // Ensure the user has an STR stat
        Task task = new Task();
        task.setType(StatType.VIT);
        task.setExperience(100); // Base experience
        AbilityEffect LifeSBounty = new AbilityEffect(AbilityType.LIFE_S_BOUNTY, 50, Duration.ofHours(6), LocalDateTime.now());
        user.setAbilityEffects(Collections.singletonList(LifeSBounty));

        // Act
        abilitiesManager.completeTask(user, task);

        // Assert
        assertEquals(150, vitStat.getCurrentExp()); // Full boosted experience to INT stat
        assertEquals(75, user.getCurrentExp()); // Half of boosted experience to overall user level
    }
    @Test
    public void testSteadfastAbility() {
        user.setMaxHp(100);
        // Given: User with 40 HP
        user.setCurrentHp(40);

        // When: Steadfast ability is applied
        abilitiesManager.applySteadfastAbility(user);

        // Then: User's HP should increase by 50, but not exceed max HP
        assertEquals(90, user.getCurrentHp(), "User's HP should be healed by 50 points");

        // Additional test to ensure HP doesn't exceed max HP
        user.setCurrentHp(95);
        abilitiesManager.applySteadfastAbility(user);
        assertEquals(100, user.getCurrentHp(), "User's HP should not exceed maximum HP");
    }


    @Test
    public void testMindSurgeAbility() {
        // Given: A user with less than maximum Mana
        user.setCurrentMana(70);
        user.setMaxMana(100);

        // When: Mind Surge ability is applied
        abilitiesManager.applyMindSurgeAbility(user);

        // Then: User's Mana should increase by 50 but not exceed max Mana
        assertEquals(100, user.getCurrentMana(), "Mana should be restored up to the max Mana");

        // Test case where Mana is close to max
        user.setCurrentMana(95); // Only 5 Mana below max
        abilitiesManager.applyMindSurgeAbility(user);
        assertEquals(100, user.getCurrentMana(), "Mana should be restored up to the max Mana, not beyond");
    }

    // Other tests as needed for different abilities and scenarios
}
