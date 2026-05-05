package com.vms.service;

import com.vms.dto.VolunteerRecommendationDto;
import com.vms.entity.Assignment;
import com.vms.entity.Event;
import com.vms.entity.Skill;
import com.vms.entity.Task;
import com.vms.entity.Volunteer;
import com.vms.repository.AssignmentRepository;
import com.vms.repository.TaskRepository;
import com.vms.repository.VolunteerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchingAndSchedulingEngineTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private VolunteerRepository volunteerRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private SchedulingService schedulingService;

    @InjectMocks
    private MatchingService matchingService;

    private Skill javaSkill;
    private Skill springSkill;
    private Volunteer volunteer;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        // Setup matching service to use the real scheduling service behavior
        matchingService = new MatchingService(taskRepository, volunteerRepository, schedulingService);

        javaSkill = new Skill(1L, "Java");
        springSkill = new Skill(2L, "Spring Boot");

        volunteer = new Volunteer();
        volunteer.setId(1L);
        volunteer.setSkills(new HashSet<>(Arrays.asList(javaSkill, springSkill)));

        Event event = new Event();
        event.setId(10L);

        task1 = new Task();
        task1.setId(100L);
        task1.setTitle("Backend API");
        task1.setEvent(event);
        task1.setSkills(new HashSet<>(Arrays.asList(javaSkill, springSkill)));
        task1.setStartTime(LocalDateTime.of(2026, 5, 10, 9, 0));
        task1.setEndTime(LocalDateTime.of(2026, 5, 10, 12, 0));

        task2 = new Task();
        task2.setId(101L);
        task2.setTitle("Database Fixes");
        task2.setEvent(event);
        task2.setSkills(new HashSet<>(Arrays.asList(javaSkill)));
        task2.setStartTime(LocalDateTime.of(2026, 5, 10, 11, 0));
        task2.setEndTime(LocalDateTime.of(2026, 5, 10, 14, 0));
    }

    @Test
    void testSchedulingConflictLogic_NoConflict() {
        // Mock existing assignment (1 PM to 4 PM)
        Task existingTask = new Task();
        existingTask.setStartTime(LocalDateTime.of(2026, 5, 10, 13, 0));
        existingTask.setEndTime(LocalDateTime.of(2026, 5, 10, 16, 0));
        Assignment assignment = new Assignment();
        assignment.setTask(existingTask);

        when(assignmentRepository.findByVolunteerIdWithTask(1L)).thenReturn(List.of(assignment));

        // New task is 9 AM to 12 PM
        boolean isAvailable = schedulingService.isVolunteerAvailable(1L, task1);
        
        // (start1 < end2) AND (start2 < end1)
        // 9:00 < 16:00 (True) AND 13:00 < 12:00 (False) => False (No conflict)
        assertTrue(isAvailable);
    }

    @Test
    void testSchedulingConflictLogic_WithConflict() {
        // Mock existing assignment (10 AM to 1 PM)
        Task existingTask = new Task();
        existingTask.setStartTime(LocalDateTime.of(2026, 5, 10, 10, 0));
        existingTask.setEndTime(LocalDateTime.of(2026, 5, 10, 13, 0));
        Assignment assignment = new Assignment();
        assignment.setTask(existingTask);

        when(assignmentRepository.findByVolunteerIdWithTask(1L)).thenReturn(List.of(assignment));

        // New task is 9 AM to 12 PM
        boolean isAvailable = schedulingService.isVolunteerAvailable(1L, task1);
        
        // (start1 < end2) AND (start2 < end1)
        // 9:00 < 13:00 (True) AND 10:00 < 12:00 (True) => True (Conflict exists)
        assertFalse(isAvailable);
    }

    @Test
    void testMatchingAlgorithm_ScoreAndSorting() {
        when(volunteerRepository.findByIdWithSkillsAndBadges(1L)).thenReturn(Optional.of(volunteer));
        when(taskRepository.findAllWithSkillsAndEvent()).thenReturn(Arrays.asList(task1, task2));
        when(assignmentRepository.findByVolunteerIdWithTask(1L)).thenReturn(List.of()); // No conflicts

        List<VolunteerRecommendationDto> recommendations = matchingService.getRecommendationsForVolunteer(1L);

        assertEquals(2, recommendations.size());
        
        // Task 1 requires Java & Spring. Volunteer has both. 2/2 = 100%
        assertEquals(100.0, recommendations.get(0).getMatchScore());
        assertEquals(100L, recommendations.get(0).getTaskId());

        // Task 2 requires Java. Volunteer has Java & Spring. 1/1 = 100% match score based on common/required
        assertEquals(100.0, recommendations.get(1).getMatchScore());
        assertEquals(101L, recommendations.get(1).getTaskId());
    }
}
