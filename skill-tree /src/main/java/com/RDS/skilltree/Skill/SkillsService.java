package com.RDS.skilltree.Skill;

import java.util.List;
import java.util.UUID;

public interface SkillsService {
    SkillDTO getSkillById(UUID id);
    SkillDTO getSkillByName(String skillName);
    List<SkillDTO> getAllSkills();
    String createSkill(SkillDRO skillDRO);
}
