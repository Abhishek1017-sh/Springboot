package com.vms.repository;

import com.vms.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.skills LEFT JOIN FETCH t.event WHERE t.id = :id")
    Optional<Task> findByIdWithSkillsAndEvent(Long id);

    @Query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.skills LEFT JOIN FETCH t.event")
    List<Task> findAllWithSkillsAndEvent();
}
