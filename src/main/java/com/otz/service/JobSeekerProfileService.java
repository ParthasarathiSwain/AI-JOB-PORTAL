package com.otz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.otz.dto.JobSeekerProfileRequest;
import com.otz.dto.JobSeekerProfileResponse;
import com.otz.entity.JobSeekerProfile;
import com.otz.entity.User;
import com.otz.repository.JobSeekerProfileRepository;
import com.otz.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository profileRepository;
    private final UserRepository userRepository;

    // Create or Update
    public JobSeekerProfileResponse createOrUpdate(Long userId, JobSeekerProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeekerProfile profile = profileRepository.findByUser(user)
                .orElse(new JobSeekerProfile());

        profile.setUser(user);
        profile.setPhone(request.getPhone());
        profile.setLocation(request.getLocation());
        profile.setSkills(request.getSkills());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setSalary(request.getSalary());
        JobSeekerProfile saved = profileRepository.save(profile);
        return mapToResponse(saved);
    }

    // Read Single
    public JobSeekerProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeekerProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToResponse(profile);
    }

    // Read All (for Admin)
    public List<JobSeekerProfileResponse> getAllProfiles() {
        return profileRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Delete
    public void deleteProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeekerProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profileRepository.delete(profile);
    }
    public Page<JobSeekerProfileResponse> searchProfiles(
            String location,
            String skills,
            Integer minExperience,
            Integer maxSalary,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // For simplicity using repository method with all filters
        List<JobSeekerProfile> filteredProfiles = profileRepository
                .findByLocationContainingIgnoreCaseAndSkillsContainingIgnoreCaseAndExperienceYearsGreaterThanEqualAndSalaryLessThanEqual(
                        location != null ? location : "",
                        skills != null ? skills : "",
                        minExperience != null ? minExperience : 0,
                        maxSalary != null ? maxSalary : Integer.MAX_VALUE
                );

        // Convert list to Page
        int start = Math.min((int) pageable.getOffset(), filteredProfiles.size());
        int end = Math.min((start + pageable.getPageSize()), filteredProfiles.size());

        List<JobSeekerProfileResponse> content = filteredProfiles.subList(start, end)
                .stream().map(this::mapToResponse).collect(Collectors.toList());

        return new PageImpl<>(content, pageable, filteredProfiles.size());
    }
    // Mapping Entity -> Response DTO
    private JobSeekerProfileResponse mapToResponse(JobSeekerProfile profile) {
        JobSeekerProfileResponse response = new JobSeekerProfileResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUser().getId());
        response.setPhone(profile.getPhone());
        response.setLocation(profile.getLocation());
        response.setSkills(profile.getSkills());
        response.setExperienceYears(profile.getExperienceYears());
        response.setSalary(profile.getSalary());
        return response;
    }
   
   
}
