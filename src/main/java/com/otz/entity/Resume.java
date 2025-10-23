package com.otz.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeekerProfile jobSeeker;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ResumePrivacy privacy = ResumePrivacy.PRIVATE;

    @Column(name = "latest_ai_score")
    private Double latestAiScore;

    @Column(name = "total_views")
    private Integer totalViews = 0;

    @Column(name = "latest_feedback", columnDefinition = "TEXT")
    private String latestFeedback;
    @Column(columnDefinition = "TEXT")
    private String matchedSkills; // skills matched with job

    @Column(columnDefinition = "TEXT")
    private String missingSkills; // skills missing compared to job
    
    @OneToOne(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ParsedResume parsedResume;

}
