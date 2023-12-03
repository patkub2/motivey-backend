package com.motivey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "armor_tiers")
public class ArmorTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_level")
    private int minLevel;

    @Column(name = "max_level")
    private int maxLevel;

    @Column(name = "armor_id")
    private String armorId;

    // Constructors, Getters, and Setters
}
