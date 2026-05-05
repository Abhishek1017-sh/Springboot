package com.vms.service;

import com.vms.dto.VolunteerRecommendationDto;
import com.vms.entity.Skill;
import com.vms.entity.Task;
import com.vms.entity.Volunteer;
import com.vms.repository.TaskRepository;
import com.vms.repository.VolunteerRepository;
import com.vms.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MatchingService {

    private final TaskRepository taskRepository;
    private final VolunteerRepository volunteerRepository;
    private final SchedulingService schedulingService;

    public MatchingService(TaskRepository taskRepository, VolunteerRepository volunteerRepository, SchedulingService schedulingService) {
        this.taskRepository = taskRepository;
        this.volunteerRepository = volunteerRepository;
        this.schedulingService = schedulingService;
    }

    /**
     * Matches volunteer skills against all available tasks.
     * Match % = (common skills / required skills)
     * Filters by availability and score > 0.
     * Returns sorted descending by score.
     */
    public List<VolunteerRecommendationDto> getRecommendationsForVolunteer(Long volunteerId) {
        Volunteer volunteer = volunteerRepository.findByIdWithSkillsAndBadges(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found"));

        Set<Skill> volunteerSkills = volunteer.getSkills();
        List<Task> allTasks = taskRepository.findAllWithSkillsAndEvent();

        List<VolunteerRecommendationDto> recommendations = new ArrayList<>();

        for (Task task : allTasks) {
            Set<Skill> requiredSkills = task.getSkills();
            
            // Skip tasks with no required skills, or handle them based on business logic (e.g. 100% match)
            if (requiredSkills.isEmpty()) {
                continue;
            }

            long matchedSkillsCount = requiredSkills.stream()
                    .filter(volunteerSkills::contains)
                    .count();

            double matchScore = (double) matchedSkillsCount / requiredSkills.size();

            if (matchScore > 0) {
                // Check Availability before recommending
                if (schedulingService.isVolunteerAvailable(volunteerId, task)) {
                    recommendations.add(new VolunteerRecommendationDto(
                            task.getId(),
                            task.getTitle(),
                            task.getEvent() != null ? task.getEvent().getId() : null,
                            matchScore * 100.0 // Percentage
                    ));
                }
            }
        }

        // Sort descending
        recommendations.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));

        return recommendations;
    }
}
