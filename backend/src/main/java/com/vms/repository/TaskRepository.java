package com.vms.repository;

import com.vms.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByEventId(Long eventId);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.skills LEFT JOIN FETCH t.event")
    List<Task> findAllWithSkillsAndEvent();
}
