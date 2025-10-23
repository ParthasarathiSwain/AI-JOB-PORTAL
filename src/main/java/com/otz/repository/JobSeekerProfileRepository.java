package com.otz.repository;

import com.otz.entity.JobSeekerProfile;
import com.otz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Long> {

    // Find profile by user
    Optional<JobSeekerProfile> findByUser(User user);

    // Optional: Search profiles by skills containing keyword
    List<JobSeekerProfile> findBySkillsContainingIgnoreCase(String skill);

    // Optional: Search profiles by location
    List<JobSeekerProfile> findByLocationContainingIgnoreCase(String location);

    // Optional: Search by experience years
    List<JobSeekerProfile> findByExperienceYearsGreaterThanEqual(int years);
    // Optional: Search by salary

    List<JobSeekerProfile> findBySalaryLessThanEqual(int salary);

    // Combined search with custom query (optional)
    List<JobSeekerProfile> findByLocationContainingIgnoreCaseAndSkillsContainingIgnoreCaseAndExperienceYearsGreaterThanEqualAndSalaryLessThanEqual(
            String location, String skills, int experienceYears, int salary
    );
}
