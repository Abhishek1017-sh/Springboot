package com.vms.controller;

import com.vms.dto.DashboardDto;
import com.vms.dto.VolunteerProfileDto;
import com.vms.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    @Autowired
    private VolunteerService volunteerService;

    // In a real app, userId would come from the JWT token via SecurityContextHolder.
    // For demo/simplicity, we might pass it as a query param or assume a logged-in user.
    // I am adding it as a request param for testing if needed, or path variable.
    
    @GetMapping("/profile")
    public ResponseEntity<VolunteerProfileDto> getProfile(@RequestParam Long userId) {
        return ResponseEntity.ok(volunteerService.getProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestParam Long userId, @RequestBody VolunteerProfileDto dto) {
        volunteerService.updateProfile(userId, dto);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDto> getDashboard(@RequestParam Long userId) {
        return ResponseEntity.ok(volunteerService.getDashboard(userId));
    }
}
