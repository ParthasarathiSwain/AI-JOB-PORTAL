package com.otz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otz.entity.SemanticEmbedding;

public interface SemanticEmbeddingRepository extends JpaRepository<SemanticEmbedding, Long> {
    List<SemanticEmbedding> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
}