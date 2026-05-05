package com.vms.service;

import com.vms.entity.Assignment;
import com.vms.entity.Task;
import com.vms.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SchedulingService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    /**
     * Checks if the volunteer is available for the given task.
     * Prevents overlapping assignments using the logic:
     * (start1 < end2) AND (start2 < end1)
     */
    public boolean isVolunteerAvailable(Long volunteerId, Task newTask) {
        List<Assignment> existingAssignments = assignmentRepository.findByVolunteerIdWithTask(volunteerId);
        
        LocalDateTime start1 = newTask.getStartTime();
        LocalDateTime end1 = newTask.getEndTime();

        for (Assignment assignment : existingAssignments) {
            Task existingTask = assignment.getTask();
            LocalDateTime start2 = existingTask.getStartTime();
            LocalDateTime end2 = existingTask.getEndTime();

            // Conflict condition: (start1 < end2) AND (start2 < end1)
            if (start1.isBefore(end2) && start2.isBefore(end1)) {
                return false; // Conflict found
            }
        }

        return true; // No conflicts
    }
}
