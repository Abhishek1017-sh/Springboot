package com.vms.controller;

import com.vms.dto.CheckInRequest;
import com.vms.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn(@RequestBody CheckInRequest request) {
        attendanceService.checkIn(request);
        return ResponseEntity.ok("Checked in successfully");
    }

    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut(@RequestParam Long volunteerId, @RequestParam Long taskId) {
        attendanceService.checkOut(volunteerId, taskId);
        return ResponseEntity.ok("Checked out successfully");
    }
}
