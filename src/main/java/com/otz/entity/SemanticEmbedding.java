package com.otz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "semantic_embeddings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemanticEmbedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long referenceId; // resumeId or jobId

    @Column(length = 20)
    private String referenceType; // "RESUME" or "JOB"

    @Column(columnDefinition = "TEXT")
    private String embeddingVector; // JSON representation of the vector
}
