package com.motivey.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_tasks")
@Getter
@Setter
public class UserTask {

    @EmbeddedId
    private UserTaskId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("taskId")
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "completion_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer completionCount = 0;
}

