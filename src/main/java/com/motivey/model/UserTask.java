package com.motivey.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_tasks")
@Getter
@Setter
@IdClass(UserTaskId.class)
public class UserTask {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "completion_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer completionCount;
}
