package com.otz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "employer_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    private String website;
    private String location;
    private String description;

    @Column(name = "logo_path")
    private String logoPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status",nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Column(name = "views")
    private Integer views = 0;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date",updatable = true,insertable = false)
    private LocalDateTime updatedDate;

    
	
    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    public enum ApprovalStatus {
        PENDING,
        VERIFIED,
        REJECTED
    }
}
