package com.motivey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stat {

    @EmbeddedId
    private StatId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "level", nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer level = 1;

    @Column(name = "current_exp", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer currentExp = 0;

    @Column(name = "max_exp", nullable = false, columnDefinition = "INT DEFAULT 4")
    private Integer maxExp = 4;



    public void addStatExperience(int exp) {
        this.currentExp += exp;
        // Implement logic to handle stat level up if currentExp >= maxExp
    }
}
