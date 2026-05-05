package com.vms.repository;

import com.vms.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByTaskId(Long taskId);
    List<Assignment> findByVolunteerId(Long volunteerId);
}
