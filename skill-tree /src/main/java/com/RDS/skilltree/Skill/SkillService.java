package com.RDS.skilltree.Skill;

import java.util.List;
import java.util.UUID;

public interface SkillService {
    SkillModel createSkill(SkillModel skill);
    SkillModel updateSkill(SkillModel skill);
    SkillModel getSkillById(UUID id);
    List<SkillDto> getAllSkills();
}
