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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserTaskRepository userTaskRepository;

    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();
        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        UserTaskId userTaskId = new UserTaskId();
        userTaskId.setUserId(user.getId());
        userTaskId.setTaskId(taskId);

        UserTask userTask = userTaskRepository.findById(userTaskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied"));


        return ResponseEntity.ok(task);
    }
    @GetMapping("/tasks")
    public ResponseEntity<?> listTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();

        // This can be a list of all tasks or filtered by the logged-in user
        List<Task> tasks = taskRepository.findAll(); // or a custom method to find by user

        return ResponseEntity.ok(tasks);
    }
    @PostMapping("/task/add")
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


    private Task convertToTaskEntity(TaskDto taskDto) {
        Task task = new Task();

        task.setName(taskDto.getName());
        task.setSection(taskDto.getSection());
        task.setDifficultyLevel(taskDto.getDifficultyLevel());
        task.setExperience(taskDto.getExperience());
        task.setType(taskDto.getType());
        task.setIcon(taskDto.getIcon());

        return task;
    }
}
