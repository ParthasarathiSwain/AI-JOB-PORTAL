package com.otz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parsed_resume")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParsedResume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(columnDefinition = "TEXT")
    private String extractedSkills;

    @Column(columnDefinition = "TEXT")
    private String extractedExperience;

    @Column(columnDefinition = "TEXT")
    private String extractedEducation;
}
