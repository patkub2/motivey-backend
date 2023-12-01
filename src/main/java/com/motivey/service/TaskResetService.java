package com.motivey.service;

import com.motivey.model.Task;
import com.motivey.model.User;
import com.motivey.model.UserTask;
import com.motivey.repository.TaskRepository;
import com.motivey.repository.UserRepository;
import com.motivey.repository.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class TaskResetService {

    @Autowired
    private UserTaskRepository userTaskRepository;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *")  // Runs every day at midnight
    @Transactional
    public void resetDailyCompletionCount() {
        List<UserTask> userTasks = userTaskRepository.findAll();

        for (UserTask userTask : userTasks) {
            if (userTask.getCompletionCount() == 0) {
                // Deduct HP from the user linked to this userTask
                User user = userTask.getUser();
                int hpDeduction = userTask.getTask().getExperience() / 4;
                user.setCurrentHp(Math.max(user.getCurrentHp() - hpDeduction, 0)); // Ensure HP doesn't go below 0
                userRepository.save(user);
            }

            // Reset completion count regardless of whether it was executed or not
            userTask.setCompletionCount(0);
            userTaskRepository.save(userTask);
        }
    }
}