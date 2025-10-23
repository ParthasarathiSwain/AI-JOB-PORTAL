package com.otz.service;

import com.otz.entity.SkillSynonym;
import com.otz.repository.SkillSynonymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillNormalizationService {

    private final SkillSynonymRepository skillSynonymRepository;

    private Map<String, String> skillMap;

    private void loadSkills() {
        skillMap = skillSynonymRepository.findAll().stream()
                .collect(Collectors.toMap(
                        s -> s.getKeyword().toLowerCase(),
                        SkillSynonym::getNormalizedSkill
                ));
    }

    public List<String> normalizeSkills(List<String> skills) {
        if (skillMap == null) loadSkills();
        return skills.stream()
                .map(s -> skillMap.getOrDefault(s.toLowerCase().trim(), s.trim()))
                .distinct()
                .toList();
    }
}
