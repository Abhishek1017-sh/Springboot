package com.vms.repository;

import com.vms.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByTargetId(Long targetId);
    List<Feedback> findByReviewerId(Long reviewerId);
}
