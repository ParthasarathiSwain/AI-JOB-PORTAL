package com.otz.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.otz.entity.EmployerProfile;

public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {
    Optional<EmployerProfile> findByUserId(Long userId);
    Page<EmployerProfile> findByLocationContainingIgnoreCase(String location, Pageable pageable);
    Page<EmployerProfile> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);
    Page<EmployerProfile> findByCompanyNameContainingIgnoreCaseAndLocationContainingIgnoreCase(
    	    String companyName, String location, Pageable pageable);

}
