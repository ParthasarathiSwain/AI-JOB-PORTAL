package com.otz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Candidate applying
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    // Job applied to
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "resume_path")
    private String resumePath;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy; // Admin/Employer updating status

    @CreationTimestamp
    @Column(name = "applied_date", nullable = false, updatable = false)
    private LocalDateTime appliedDate;

    @UpdateTimestamp
    @Column(name = "updated_date",updatable = true,insertable=false)
    private LocalDateTime updatedDate;

    public enum ApplicationStatus {
        APPLIED,
        SHORTLISTED,
        REJECTED,
        HIRED
    }
}
