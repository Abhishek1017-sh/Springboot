package com.vms.service;

import com.vms.dto.FeedbackDto;
import com.vms.entity.Feedback;
import com.vms.entity.User;
import com.vms.repository.FeedbackRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private EntityManager entityManager;

    public void submitFeedback(FeedbackDto dto) {
        Feedback feedback = new Feedback();
        
        User reviewer = entityManager.getReference(User.class, dto.getReviewerId());
        User target = entityManager.getReference(User.class, dto.getTargetId());
        
        feedback.setReviewer(reviewer);
        feedback.setTarget(target);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        
        feedbackRepository.save(feedback);
    }

    public List<FeedbackDto> getFeedbackForUser(Long userId) {
        List<Feedback> feedbacks = feedbackRepository.findByTargetId(userId);
        return feedbacks.stream().map(f -> {
            FeedbackDto dto = new FeedbackDto();
            dto.setId(f.getId());
            dto.setReviewerId(f.getReviewer().getId());
            dto.setTargetId(f.getTarget().getId());
            dto.setRating(f.getRating());
            dto.setComment(f.getComment());
            dto.setCreatedAt(f.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }
}
