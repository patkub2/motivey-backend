package com.motivey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    private String email;
    private Integer characterLevel;
    private Integer maxExp;
    private Integer currentExp;
    private Integer maxHp;
    private Integer currentHp;
    private Integer maxMana;
    private Integer currentMana;
    private String currentArmorId; // Only the armorId of the currentArmor
    private StatDto intStat;
    private StatDto strStat;
    private StatDto agiStat;
    private StatDto vitStat;
    // Constructors, Getters, and Setters
}
