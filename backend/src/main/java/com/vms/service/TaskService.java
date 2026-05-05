package com.vms.service;

import com.vms.dto.ApplicationDto;
import com.vms.dto.TaskDto;
import com.vms.entity.Application;
import com.vms.entity.ApplicationStatus;
import com.vms.entity.Assignment;
import com.vms.entity.Event;
import com.vms.entity.Task;
import com.vms.entity.Volunteer;
import com.vms.entity.Skill;
import com.vms.repository.ApplicationRepository;
import com.vms.repository.AssignmentRepository;
import com.vms.repository.EventRepository;
import com.vms.repository.TaskRepository;
import com.vms.repository.VolunteerRepository;
import com.vms.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;
    private final ApplicationRepository applicationRepository;
    private final AssignmentRepository assignmentRepository;
    private final VolunteerRepository volunteerRepository;
    private final SkillRepository skillRepository;

    @Transactional
    public TaskDto createTask(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStartTime(taskDto.getStartTime());
        task.setEndTime(taskDto.getEndTime());

        Event event = eventRepository.findById(taskDto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));
        task.setEvent(event);

        if (taskDto.getRequiredSkillIds() != null) {
            Set<Skill> skills = new HashSet<>(skillRepository.findAllById(taskDto.getRequiredSkillIds()));
            task.setSkills(skills);
        }

        task = taskRepository.save(task);
        taskDto.setId(task.getId());
        return taskDto;
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream().map(task -> {
            TaskDto dto = new TaskDto();
            dto.setId(task.getId());
            dto.setEventId(task.getEvent().getId());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setStartTime(task.getStartTime());
            dto.setEndTime(task.getEndTime());
            if (task.getSkills() != null) {
                dto.setRequiredSkillIds(task.getSkills().stream().map(Skill::getId).collect(Collectors.toSet()));
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public ApplicationDto applyForTask(ApplicationDto applicationDto) {
        Application application = new Application();
        Volunteer volunteer = volunteerRepository.findById(applicationDto.getVolunteerId())
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        Task task = taskRepository.findById(applicationDto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        application.setVolunteer(volunteer);
        application.setTask(task);
        application.setStatus(ApplicationStatus.PENDING);

        application = applicationRepository.save(application);
        applicationDto.setId(application.getId());
        applicationDto.setStatus(application.getStatus());
        return applicationDto;
    }

    @Transactional
    public void approveApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        application.setStatus(ApplicationStatus.APPROVED);
        applicationRepository.save(application);

        Assignment assignment = new Assignment();
        assignment.setVolunteer(application.getVolunteer());
        assignment.setTask(application.getTask());
        assignmentRepository.save(assignment);
    }

    @Transactional
    public void rejectApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        application.setStatus(ApplicationStatus.REJECTED);
        applicationRepository.save(application);
    }
}
