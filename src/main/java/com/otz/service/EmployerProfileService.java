package com.otz.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.otz.dto.EmployerProfileRequest;
import com.otz.dto.EmployerProfileResponse;
import com.otz.entity.EmployerProfile;
import com.otz.entity.User;
import com.otz.exception.ApiException;
import com.otz.repository.EmployerProfileRepository;
import com.otz.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployerProfileService {

    private final EmployerProfileRepository repository;
    private final UserRepository userRepository;
    private final String uploadDir = "C:/Users/PC/SpringBoot-WorkSpace/Ai-Job-Portal/uploads/employers";

    // Create or Update profile
    public EmployerProfileResponse createOrUpdateProfile(Long userId, EmployerProfileRequest request) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", 404));

        EmployerProfile profile = repository.findByUserId(userId)
                .orElse(new EmployerProfile());

        profile.setUser(user);
        profile.setCompanyName(request.getCompanyName());
        profile.setWebsite(request.getWebsite());
        profile.setLocation(request.getLocation());
        profile.setDescription(request.getDescription());
        profile.setApprovalStatus(EmployerProfile.ApprovalStatus.PENDING);

        MultipartFile logo = request.getLogo();
        if (logo != null && !logo.isEmpty()) {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            String fileName = System.currentTimeMillis() + "_" + logo.getOriginalFilename();
            logo.transferTo(new File(uploadDir + fileName));
            profile.setLogoPath(fileName);
        }

        EmployerProfile saved = repository.save(profile);
        return mapToResponse(saved);
    }

    // Get own profile
    public EmployerProfileResponse getProfile(Long userId) {
        EmployerProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("Employer profile not found", 404));

        profile.setViews(profile.getViews() + 1);
        repository.save(profile);
        return mapToResponse(profile);
    }

    // Delete profile
    public void deleteProfile(Long userId) {
        EmployerProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("Employer profile not found", 404));
        repository.delete(profile);
    }

    // List all employers
    public List<EmployerProfileResponse> getAllProfiles() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Search with pagination
    public Page<EmployerProfileResponse> searchProfiles(String companyName, String location, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<EmployerProfile> profiles;

        if (companyName != null && !companyName.isEmpty() && location != null && !location.isEmpty()) {
            profiles = repository.findByCompanyNameContainingIgnoreCaseAndLocationContainingIgnoreCase(companyName, location, pageable);
        } else if (companyName != null && !companyName.isEmpty()) {
            profiles = repository.findByCompanyNameContainingIgnoreCase(companyName, pageable);
        } else if (location != null && !location.isEmpty()) {
            profiles = repository.findByLocationContainingIgnoreCase(location, pageable);
        } else {
            profiles = repository.findAll(pageable);
        }

        return profiles.map(this::mapToResponse);
    }

    // Admin: Approve/Reject profile
    public EmployerProfileResponse updateApprovalStatus(Long profileId, EmployerProfile.ApprovalStatus status, Long adminId) {
        EmployerProfile profile = repository.findById(profileId)
                .orElseThrow(() -> new ApiException("Employer profile not found", 404));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ApiException("Admin not found", 404));

        profile.setApprovalStatus(status);
        profile.setApprovedBy(admin);

        return mapToResponse(repository.save(profile));
    }

    // Map entity to DTO
    private EmployerProfileResponse mapToResponse(EmployerProfile profile) {
        EmployerProfileResponse response = new EmployerProfileResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUser().getId());
        response.setCompanyName(profile.getCompanyName());
        response.setWebsite(profile.getWebsite());
        response.setLocation(profile.getLocation());
        response.setDescription(profile.getDescription());
        response.setLogoPath(profile.getLogoPath());
        response.setApprovalStatus(profile.getApprovalStatus());
        response.setViews(profile.getViews());
        response.setCreatedDate(profile.getCreatedDate());
        response.setUpdatedDate(profile.getUpdatedDate());
        response.setApprovedBy(profile.getApprovedBy() != null ? profile.getApprovedBy().getId() : null);
        return response;
    }
}
