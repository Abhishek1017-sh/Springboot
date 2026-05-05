package com.vms.controller;

import com.vms.dto.ApplicationDto;
import com.vms.dto.TaskDto;
import com.vms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/admin/task")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.createTask(taskDto));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping("/tasks/apply")
    public ResponseEntity<ApplicationDto> applyForTask(@RequestBody ApplicationDto applicationDto) {
        return ResponseEntity.ok(taskService.applyForTask(applicationDto));
    }

    @PostMapping("/admin/applications/{id}/approve")
    public ResponseEntity<Void> approveApplication(@PathVariable Long id) {
        taskService.approveApplication(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/applications/{id}/reject")
    public ResponseEntity<Void> rejectApplication(@PathVariable Long id) {
        taskService.rejectApplication(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tasks/{taskId}/status")
    public ResponseEntity<?> getApplicationStatus(@PathVariable Long taskId, @RequestParam Long volunteerId) {
        return ResponseEntity.ok(taskService.getApplicationStatus(volunteerId, taskId));
    }
}
