package com.vms.service;

import com.vms.dto.CheckInRequest;
import com.vms.entity.Attendance;
import com.vms.entity.Task;
import com.vms.entity.Volunteer;
import com.vms.repository.AttendanceRepository;
import com.vms.repository.TaskRepository;
import com.vms.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final VolunteerRepository volunteerRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public void checkIn(CheckInRequest request) {
        Volunteer volunteer = volunteerRepository.findById(request.getVolunteerId())
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Attendance attendance = Attendance.builder()
                .volunteer(volunteer)
                .task(task)
                .checkInTime(LocalDateTime.now())
                .location(request.getLatitude() + "," + request.getLongitude())
                .build();

        attendanceRepository.save(attendance);
    }

    @Transactional
    public void checkOut(Long volunteerId, Long taskId) {
        Attendance attendance = attendanceRepository.findByVolunteerIdAndTaskId(volunteerId, taskId)
                .orElseThrow(() -> new RuntimeException("Check-in record not found"));

        attendance.setCheckOutTime(LocalDateTime.now());
        attendanceRepository.save(attendance);
        
        // Logic to update total hours in Volunteer profile could go here
    }
}
