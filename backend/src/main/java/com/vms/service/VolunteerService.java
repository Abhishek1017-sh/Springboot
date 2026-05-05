package com.vms.service;

import com.vms.dto.*;
import com.vms.entity.*;
import com.vms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.List;
import java.util.HashSet;

@Service
@Transactional
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;
    
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    public VolunteerProfileDto getProfile(Long userId) {
        Volunteer volunteer = volunteerRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Volunteer not found"));
            
        VolunteerProfileDto dto = new VolunteerProfileDto();
        dto.setId(volunteer.getUserId());
        dto.setName(volunteer.getUser().getName());
        dto.setEmail(volunteer.getUser().getEmail());
        dto.setTotalHours(volunteer.getTotalHours());
        dto.setRating(volunteer.getRating());
        
        if (volunteer.getSkills() != null) {
            dto.setSkills(volunteer.getSkills().stream().map(s -> {
                SkillDto sdto = new SkillDto();
                sdto.setId(s.getId());
                sdto.setName(s.getName());
                return sdto;
            }).collect(Collectors.toList()));
        }

        if (volunteer.getAvailabilities() != null) {
            dto.setAvailabilities(volunteer.getAvailabilities().stream().map(a -> {
                AvailabilityDto adto = new AvailabilityDto();
                adto.setId(a.getId());
                adto.setDayOfWeek(a.getDayOfWeek());
                adto.setStartTime(a.getStartTime());
                adto.setEndTime(a.getEndTime());
                return adto;
            }).collect(Collectors.toList()));
        }
        
        return dto;
    }

    public void updateProfile(Long userId, VolunteerProfileDto dto) {
        Volunteer volunteer = volunteerRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Volunteer not found"));
            
        // Update basic user info if needed (assuming user entity exists)
        User user = volunteer.getUser();
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());

        // Update skills
        if (dto.getSkills() != null) {
            volunteer.setSkills(new HashSet<>());
            for (SkillDto skillDto : dto.getSkills()) {
                Skill skill = skillRepository.findByName(skillDto.getName())
                    .orElseGet(() -> {
                        Skill newSkill = new Skill();
                        newSkill.setName(skillDto.getName());
                        return skillRepository.save(newSkill);
                    });
                volunteer.getSkills().add(skill);
            }
        }
        
        // Update availability
        if (dto.getAvailabilities() != null) {
            availabilityRepository.deleteByVolunteerUserId(userId);
            for (AvailabilityDto adto : dto.getAvailabilities()) {
                Availability availability = new Availability();
                availability.setVolunteer(volunteer);
                availability.setDayOfWeek(adto.getDayOfWeek());
                availability.setStartTime(adto.getStartTime());
                availability.setEndTime(adto.getEndTime());
                availabilityRepository.save(availability);
            }
        }
        
        volunteerRepository.save(volunteer);
    }
    
    public DashboardDto getDashboard(Long userId) {
        Volunteer volunteer = volunteerRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Volunteer not found"));
            
        DashboardDto dto = new DashboardDto();
        dto.setTotalHoursWorked(volunteer.getTotalHours());
        dto.setAverageRating(volunteer.getRating());
        // For gamification, member 4 or 2 can implement badge assignment logic
        dto.setBadges(List.of("Newbie")); // Placeholder
        return dto;
    }
}
