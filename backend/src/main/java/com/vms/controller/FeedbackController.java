package com.vms.controller;

import com.vms.dto.FeedbackDto;
import com.vms.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<String> submitFeedback(@RequestBody FeedbackDto dto) {
        feedbackService.submitFeedback(dto);
        return ResponseEntity.ok("Feedback submitted successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FeedbackDto>> getFeedbackForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(feedbackService.getFeedbackForUser(userId));
    }
}
