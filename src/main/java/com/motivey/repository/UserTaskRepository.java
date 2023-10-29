package com.motivey.repository;

import com.motivey.model.UserTask;
import com.motivey.model.UserTaskId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, UserTaskId> {
    // You can define custom query methods here if needed
}
