package com.otz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skill_synonyms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillSynonym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String keyword; // Example: JS, ReactJS, Node

    @Column(name = "normalized_skill", nullable = false)
    private String normalizedSkill; // Example: JavaScript, React, Node.js
}
