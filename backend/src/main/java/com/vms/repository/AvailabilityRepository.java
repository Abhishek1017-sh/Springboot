package com.vms.repository;

import com.vms.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByVolunteerUserId(Long volunteerId);
    void deleteByVolunteerUserId(Long volunteerId);
}
