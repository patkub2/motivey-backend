package com.motivey.controller;

import com.motivey.dto.TaskDto;

import com.motivey.exception.UsernameNotFoundException;
import com.motivey.model.Task;
import com.motivey.model.User;
import com.motivey.model.UserTask;
import com.motivey.model.UserTaskId;
import com.motivey.repository.TaskRepository;
import com.motivey.repository.UserRepository;

import com.motivey.repository.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserTaskRepository userTaskRepository;

    @PostMapping("/task")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            String loggedInUserEmail = authentication.getName();

            // Fetch the user by email
            User user = userRepository.findByEmail(loggedInUserEmail).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            // Convert TaskDto to Task Entity and save
            Task task = convertToTaskEntity(taskDto);
            task = taskRepository.save(task);

            // Link user and task via UserTask entity
            UserTask userTask = new UserTask();
            UserTaskId userTaskId = new UserTaskId();
            userTaskId.setUserId(user.getId());
            userTaskId.setTaskId(task.getId());
            userTask.setId(userTaskId);
            userTask.setUser(user);
            userTask.setTask(task);

            // Save the UserTask entity
            userTaskRepository.save(userTask);

            return new ResponseEntity<>(task, HttpStatus.CREATED);
        }

        return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
    }

    // Utility method to convert TaskDto to Task Entity
    private Task convertToTaskEntity(TaskDto taskDto) {
        Task task = new Task();
        // map all properties from taskDto to task, e.g.
        task.setName(taskDto.getName());
        task.setSection(taskDto.getSection());
        // ... map other properties
        return task;
    }
}
