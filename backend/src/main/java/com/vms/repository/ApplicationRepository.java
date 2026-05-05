package com.vms.repository;

import com.vms.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query("SELECT a FROM Application a LEFT JOIN FETCH a.task t LEFT JOIN FETCH a.volunteer v WHERE v.id = :volunteerId")
    List<Application> findByVolunteerIdWithTask(Long volunteerId);
    
    @Query("SELECT a FROM Application a LEFT JOIN FETCH a.volunteer v LEFT JOIN FETCH a.task t WHERE t.id = :taskId")
    List<Application> findByTaskIdWithVolunteer(Long taskId);
    
    boolean existsByVolunteerIdAndTaskId(Long volunteerId, Long taskId);
}
