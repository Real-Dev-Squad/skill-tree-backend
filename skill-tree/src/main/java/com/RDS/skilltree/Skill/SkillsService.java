package com.RDS.skilltree.Skill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SkillsService {
    SkillDTO getSkillById(UUID id);
    SkillDTO getSkillByName(String skillName);
    Page<SkillDTO> getAllSkills(Pageable pageable);
    SkillDTO createSkill(SkillDRO skillDRO);
}
