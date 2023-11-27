package com.motivey;

import com.motivey.model.User;
import com.motivey.model.AbilityEffect;
import com.motivey.enums.Ability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {


    private User user;
    @BeforeEach
    public void setup() {
        // Initialize the user object before each test
        user = new User();
        // Other initializations if needed
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
        user.regenerateHpAndMana();

        // Assertions
        // Base regen is 5 mana per hour, plus 10 from ARCANE_INSIGHT, so expect 65 mana after an hour
        assertEquals(65, user.getCurrentMana());
    }

    @Test
    public void testHpRegenerationWithIronResolveEffect() {
        // Given: User with Iron Resolve effect active
        AbilityEffect ironResolveEffect = new AbilityEffect(Ability.IRON_RESOLVE, 10, Duration.ofHours(6), LocalDateTime.now()); // Additional 10 HP per hour
        user.setAbilityEffects(Collections.singletonList(ironResolveEffect));
        user.setCurrentHp(50);
        user.setMaxHp(100);
        user.setLastHpUpdate(LocalDateTime.now().minusHours(1)); // Last updated an hour ago

        // When: Regenerate HP is called
        user.regenerateHpAndMana();

        // Then: HP should increase by 15 (5 base regen + 10 from Iron Resolve)
        assertEquals(65, user.getCurrentHp());
    }

    // Other tests as needed for different abilities and scenarios
}
