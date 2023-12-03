package com.motivey;

import com.motivey.model.Task;
import com.motivey.model.User;
import com.motivey.model.UserTask;
import com.motivey.repository.UserRepository;
import com.motivey.repository.UserTaskRepository;
import com.motivey.service.TaskResetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskResetServiceTest {

    @Mock
    private UserTaskRepository userTaskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskResetService taskResetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testResetDailyCompletionCount() {
        // Setup
        User userWithTaskNotCompleted = new User();
        userWithTaskNotCompleted.setCurrentHp(100);

        User userWithTaskCompleted = new User();
        userWithTaskCompleted.setCurrentHp(100);

        Task task = new Task();
        task.setExperience(40);  // Assuming experience is set to 40

        UserTask userTaskNotCompleted = new UserTask();
        userTaskNotCompleted.setUser(userWithTaskNotCompleted);
        userTaskNotCompleted.setTask(task);
        userTaskNotCompleted.setCompletionCount(0);

        UserTask userTaskCompleted = new UserTask();
        userTaskCompleted.setUser(userWithTaskCompleted);
        userTaskCompleted.setTask(task);
        userTaskCompleted.setCompletionCount(1);

        List<UserTask> userTasks = Arrays.asList(userTaskNotCompleted, userTaskCompleted);

        // Mocking
        when(userTaskRepository.findAll()).thenReturn(userTasks);

        // Act
        taskResetService.resetDailyCompletionCount();

        // Assert
        verify(userRepository, times(1)).save(userWithTaskNotCompleted);
        verify(userRepository, never()).save(userWithTaskCompleted);
        assertEquals(90, userWithTaskNotCompleted.getCurrentHp());
        assertEquals(100, userWithTaskCompleted.getCurrentHp());
        verify(userTaskRepository, times(2)).save(any(UserTask.class));
    }

    // ... Test methods will be here
}
