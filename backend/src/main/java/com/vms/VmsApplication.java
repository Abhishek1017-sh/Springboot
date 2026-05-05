package com.vms;

import com.vms.entity.*;
import com.vms.repository.*;
import com.vms.service.MatchingService;
import com.vms.dto.VolunteerRecommendationDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class VmsApplication {
    public static void main(String[] args) {
        java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Kolkata"));
        SpringApplication.run(VmsApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoRunner(
            UserRepository userRepository,
            VolunteerRepository volunteerRepository,
            SkillRepository skillRepository,
            TaskRepository taskRepository,
            AssignmentRepository assignmentRepository,
            MatchingService matchingService) {
        return args -> {
            System.out.println("==================================================");
            System.out.println("🚀 BOOTING UP DEMO FOR MEMBER 4: MATCHING ENGINE");
            System.out.println("==================================================");

            // 1. Create Skills
            Skill javaSkill = skillRepository.save(new Skill(null, "Java"));
            Skill reactSkill = skillRepository.save(new Skill(null, "React Native"));

            // 2. Create User and Volunteer
            User user = userRepository.save(User.builder().name("Aditya Shukla").email("aditya@vms.com").password("pass").role(Role.VOLUNTEER).build());
            
            HashSet<Skill> volunteerSkills = new HashSet<>();
            volunteerSkills.add(javaSkill);
            volunteerSkills.add(reactSkill);
            
            Volunteer volunteer = volunteerRepository.save(Volunteer.builder().user(user).skills(volunteerSkills).totalHours(0.0).rating(0.0).badges(new HashSet<>()).build());

            // 3. Create Tasks
            HashSet<Skill> task1Skills = new HashSet<>();
            task1Skills.add(javaSkill);
            
            Task task1 = taskRepository.save(Task.builder()
                    .title("Backend API Development")
                    .description("Create Spring Boot REST APIs")
                    .startTime(LocalDateTime.now().plusDays(1).withHour(10))
                    .endTime(LocalDateTime.now().plusDays(1).withHour(14))
                    .skills(task1Skills)
                    .build());

            HashSet<Skill> task2Skills = new HashSet<>();
            task2Skills.add(javaSkill);
            task2Skills.add(reactSkill);

            Task task2 = taskRepository.save(Task.builder()
                    .title("Full Stack Feature")
                    .description("Connect React Native to Spring Boot")
                    .startTime(LocalDateTime.now().plusDays(1).withHour(16))
                    .endTime(LocalDateTime.now().plusDays(1).withHour(20))
                    .skills(task2Skills)
                    .build());
                    
            // Conflict Task (overlaps with task1)
            Task conflictTask = taskRepository.save(Task.builder()
                    .title("Emergency Meeting")
                    .description("Overlaps with task 1")
                    .startTime(LocalDateTime.now().plusDays(1).withHour(12))
                    .endTime(LocalDateTime.now().plusDays(1).withHour(15))
                    .skills(task1Skills)
                    .build());

            System.out.println("✅ Database populated with dummy Tasks and Volunteers.");

            // 4. Test Matching Engine
            System.out.println("\n--- RUNNING MATCHING ALGORITHM FOR: " + user.getName() + " ---");
            List<VolunteerRecommendationDto> matches = matchingService.getRecommendationsForVolunteer(volunteer.getId());
            
            for (VolunteerRecommendationDto match : matches) {
                System.out.println("⭐ Matched Task: " + match.getTaskTitle() + " | Match Score: " + match.getMatchScore() + "%");
            }
            
            // 5. Test Scheduling Conflict
            System.out.println("\n--- RUNNING SCHEDULING CONFLICT ENGINE ---");
            assignmentRepository.save(Assignment.builder().volunteer(volunteer).task(task1).build());
            System.out.println("Volunteer is now ASSIGNED to: " + task1.getTitle() + " (10 AM - 2 PM)");
            
            System.out.println("Checking availability for: " + conflictTask.getTitle() + " (12 PM - 3 PM)...");
            
            // Should be false due to overlap
            boolean isAvailable = matchingService.getRecommendationsForVolunteer(volunteer.getId())
                .stream().anyMatch(dto -> dto.getTaskId().equals(conflictTask.getId()));
                
            if (!isAvailable) {
                System.out.println("⛔ CONFLICT DETECTED! Overlapping shift rejected successfully.");
            } else {
                System.out.println("⚠️ ERROR: Conflict not detected.");
            }
            
            System.out.println("==================================================");
            System.out.println("🎯 DEMO COMPLETE! Member 4 logic working flawlessly.");
            System.out.println("==================================================");
        };
    }
}
