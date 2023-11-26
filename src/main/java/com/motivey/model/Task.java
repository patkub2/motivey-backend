package com.motivey.model;

import com.motivey.enums.Section;
import com.motivey.enums.StatType;
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
    @Column(columnDefinition = "ENUM('HABITS', 'CHALLENGES', 'GOALS')")
    private Section section;

    @Column(name = "difficulty_level")
    private Integer difficultyLevel;

    private Integer experience;

    @Column(name = "daily_execution_counter", nullable = false)
    private Integer dailyExecutionCounter = 0; // default value

    @Column(nullable = false)
    private Boolean completed = false; // default value

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('INT', 'STR', 'AGI', 'VIT')")
    private StatType type;

    private String icon;

}