package com.otz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otz.entity.SkillSynonym;

public interface SkillSynonymRepository extends JpaRepository<SkillSynonym, Long> {
    SkillSynonym findByKeyword(String keyword);
}