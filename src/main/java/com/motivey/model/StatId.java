package com.motivey.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class StatId implements Serializable {
    private Long userId;
    private String type;

    // Default Constructor
    public StatId() {}

    // Parameterized Constructor
    public StatId(Long userId, String type) {
        this.userId = userId;
        this.type = type;
    }


    // hashCode() and equals() methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatId statId = (StatId) o;
        return Objects.equals(userId, statId.userId) &&
                Objects.equals(type, statId.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, type);
    }
}
