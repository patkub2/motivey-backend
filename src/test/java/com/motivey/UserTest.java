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

    // Other tests as needed for different abilities and scenarios
}
