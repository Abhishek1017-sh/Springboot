package com.vms.repository;

import com.vms.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    @Query("SELECT a FROM Assignment a LEFT JOIN FETCH a.task t WHERE a.volunteer.id = :volunteerId")
    List<Assignment> findByVolunteerIdWithTask(Long volunteerId);
    
    List<Assignment> findByTaskId(Long taskId);
    
    boolean existsByVolunteerIdAndTaskId(Long volunteerId, Long taskId);
}
