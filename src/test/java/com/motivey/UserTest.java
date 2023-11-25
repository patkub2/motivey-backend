package com.motivey;

import com.motivey.model.StackingEffect;
import com.motivey.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
public class UserTest {

    @Test
    public void testRegenerateHpAndMana_NoEffects() {
        User user = new User();
        user.setMaxHp(100);
        user.setCurrentHp(50);
        user.setLastHpUpdate(LocalDateTime.now().minusHours(2));

        user.regenerateHpAndMana();

        assertEquals(60, user.getCurrentHp()); // Assuming 5 HP regen per hour
    }

    @Test
    public void testAddStackingEffect() {
        User user = new User();
        StackingEffect effect = new StackingEffect(StackingEffect.EffectType.HP_REGEN, 2, LocalDateTime.now().plusHours(6));

        user.addStackingEffect(effect);

        assertEquals(1, user.getStackingEffects().size());
    }

    @Test
    public void testExpiredEffectsIgnored() {
        User user = new User();
        user.setCurrentHp(50);
        user.setMaxHp(100);
        user.setLastHpUpdate(LocalDateTime.now().minusHours(4));

        // Expired effect
        StackingEffect expiredEffect = new StackingEffect(StackingEffect.EffectType.HP_REGEN, 10, LocalDateTime.now().minusHours(1));
        user.addStackingEffect(expiredEffect);

        user.regenerateHpAndMana();

        // Only standard regeneration should apply, not the expired effect
        assertEquals(70, user.getCurrentHp()); // 4 hours * 5 HP/hour
    }
    @Test
    public void testOverlappingEffects() {
        User user = new User();
        user.setCurrentMana(50);
        user.setMaxMana(100);
        user.setLastManaUpdate(LocalDateTime.now().minusHours(2));

        // Two overlapping effects
        StackingEffect effect1 = new StackingEffect(StackingEffect.EffectType.MANA_REGEN, 3, LocalDateTime.now().plusHours(2));
        StackingEffect effect2 = new StackingEffect(StackingEffect.EffectType.MANA_REGEN, 2, LocalDateTime.now().plusHours(1));
        user.addStackingEffect(effect1);
        user.addStackingEffect(effect2);

        user.regenerateHpAndMana();

        // Total regen = (5 base + 5 total boost) * 2 hours
        assertEquals(70, user.getCurrentMana());
    }
    @Test
    public void testRegenerationAtFullHealth() {
        User user = new User();
        user.setCurrentHp(100);
        user.setMaxHp(100);
        user.setLastHpUpdate(LocalDateTime.now().minusHours(2));

        user.regenerateHpAndMana();

        assertEquals(100, user.getCurrentHp()); // HP should not exceed max
    }


}
