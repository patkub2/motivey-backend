package com.motivey.model;

import javax.persistence.OneToMany;

import com.motivey.enums.Ability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "character_level", nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer characterLevel = 1;

    @Column(name = "max_exp", nullable = false, columnDefinition = "INT DEFAULT 100")
    private Integer maxExp = 100;

    @Column(name = "current_exp", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer currentExp = 0;

    @Column(name = "max_hp", nullable = false, columnDefinition = "INT DEFAULT 100")
    private Integer maxHp = 100;

    @Column(name = "current_hp", nullable = false, columnDefinition = "INT DEFAULT 100")
    private Integer currentHp = 100;

    @Column(name = "max_mana", nullable = false, columnDefinition = "INT DEFAULT 100")
    private Integer maxMana = 100;

    @Column(name = "current_mana", nullable = false, columnDefinition = "INT DEFAULT 100")
    private Integer currentMana = 100;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer gold = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stat> stats = new ArrayList<>();

    @Column(name = "last_hp_update", nullable = true)
    private LocalDateTime lastHpUpdate;

    @Column(name = "last_mana_update", nullable = true)
    private LocalDateTime lastManaUpdate;

    @ElementCollection
    @CollectionTable(name = "user_ability_effects", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "ability_effect")
    private List<AbilityEffect> abilityEffects = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private static final int BASE_EXP = 100;
    private static final double GROWTH_RATE = 1.2; // Adjust this rate as needed
    public void addExperience(int exp) {
        this.currentExp += exp;

        while (this.currentExp >= this.maxExp) {
            // Level up
            this.characterLevel++;
            this.currentExp -= this.maxExp;

            // Update maxExp for the next level
            this.maxExp = (int)(BASE_EXP * Math.pow(GROWTH_RATE, this.characterLevel - 1));
        }

        // Additional logic for other effects of leveling up can be added here
    }
    public void regenerateHpAndMana() {
        LocalDateTime now = LocalDateTime.now();
        int manaRegenRate = calculateManaRegenerationRate(now);
        int hpRegenRate = calculateHpRegenerationRate(now);

        // Apply HP regeneration
        if (lastHpUpdate != null) {
            long hoursElapsed = ChronoUnit.HOURS.between(lastHpUpdate, now);
            this.currentHp = Math.toIntExact(Math.min(this.maxHp, this.currentHp + hpRegenRate * hoursElapsed));
        }

        // Apply Mana regeneration
        if (lastManaUpdate != null) {
            long hoursElapsed = ChronoUnit.HOURS.between(lastManaUpdate, now);
            this.currentMana = Math.toIntExact(Math.min(this.maxMana, this.currentMana + manaRegenRate * hoursElapsed));
        }

        this.lastHpUpdate = now;
        this.lastManaUpdate = now;
    }

    private int calculateManaRegenerationRate(LocalDateTime now) {
        int baseRate = 5;
        for (AbilityEffect effect : this.abilityEffects) {
            if (effect.getAbilityType() == Ability.ARCANE_INSIGHT && effect.isActive(now)) {
                return baseRate + effect.getEffectMagnitude();
            }
        }
        return baseRate;
    }

    private int calculateHpRegenerationRate(LocalDateTime now) {
        int baseRate = 5;
        for (AbilityEffect effect : this.abilityEffects) {
            if (effect.getAbilityType() == Ability.IRON_RESOLVE && effect.isActive(now)) {
                return baseRate + effect.getEffectMagnitude();
            }
        }
        return baseRate;
    }
}