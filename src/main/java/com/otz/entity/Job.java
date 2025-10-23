package com.otz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    @Column(nullable = false)
    private String title;

    private String description;
    private String location;
    private String skills;

    private Integer minExperience; // years
    private Integer maxSalary;

    @Column(name = "banner_path")
    private String bannerPath; // new field for job banner/photo

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Column(name = "views")
    private Integer views = 0;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date", updatable = true, insertable = false)
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    public enum ApprovalStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
