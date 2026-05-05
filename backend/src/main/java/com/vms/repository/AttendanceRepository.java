package com.vms.repository;

import com.vms.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByVolunteerId(Long volunteerId);
    Optional<Attendance> findByVolunteerIdAndTaskId(Long volunteerId, Long taskId);
}
