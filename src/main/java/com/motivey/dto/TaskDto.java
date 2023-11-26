package com.motivey.dto;

import com.motivey.enums.Section;
import com.motivey.enums.StatType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {
    private String name;
    private Section section;
    private Integer difficultyLevel;
    private Integer experience;
    private StatType type;
    private String icon;
}

