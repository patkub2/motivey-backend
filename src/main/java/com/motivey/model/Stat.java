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

    @Column(name = "max_exp", nullable = false, columnDefinition = "INT DEFAULT 100")
    private Integer maxExp = 100;


    public void addStatExperience(int exp) {
        this.currentExp += exp;

        while (this.currentExp >= this.maxExp) {
            // Level up
            this.level++;
            this.currentExp -= this.maxExp;

        }

        // Additional logic for other effects of leveling up can be added here
    }

}
