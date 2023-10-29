package com.motivey.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('habbits', 'challenges', 'goals')")
    private Section section;

    @Column(name = "difficulty_level")
    private Integer difficultyLevel;

    private Integer experience;

    @Column(name = "daily_execution_counter", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer dailyExecutionCounter;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('int', 'str', 'agi', 'vit')")
    private Stat.StatType type;

    private String icon;

    public enum Section {
        HABBITS, CHALLENGES, GOALS
    }
}
