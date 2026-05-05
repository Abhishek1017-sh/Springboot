package com.vms.repository;

import com.vms.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findByUserId(Long userId);

    @Query("SELECT v FROM Volunteer v LEFT JOIN FETCH v.skills LEFT JOIN FETCH v.badges WHERE v.id = :id")
    Optional<Volunteer> findByIdWithSkillsAndBadges(Long id);
    
    @Query("SELECT v FROM Volunteer v LEFT JOIN FETCH v.skills")
    List<Volunteer> findAllWithSkills();
}
