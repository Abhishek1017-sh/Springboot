package com.vms.repository;

import com.vms.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByTaskId(Long taskId);
    List<Application> findByVolunteerId(Long volunteerId);
    java.util.Optional<com.vms.entity.Application> findByVolunteerIdAndTaskId(Long volunteerId, Long taskId);
}
