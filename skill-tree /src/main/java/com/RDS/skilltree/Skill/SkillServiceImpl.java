package com.RDS.skilltree.Skill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class SkillServiceImpl implements SkillService {
    @Autowired
    private SkillRepository skillRepository;
    @Override
    public SkillModel createSkill(SkillModel skill) {
        skill.setCreatedAt(Instant.now());
        skillRepository.save(skill);
        return skill;
    }

    @Override
    public SkillModel updateSkill(SkillModel skill) {
        return null;
    }

    @Override
    public SkillModel getSkillById(UUID id) {
        SkillModel skill = new SkillModel();
        try {
            skill =  skillRepository.findById(id).get();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return skill;
    }

    @Override
    public List<SkillDto> getAllSkills() {
        List<SkillModel> skillModels =  skillRepository.findAll();
        return skillModels.stream().map(
                skill -> new SkillDto(
                        skill.getId(),
                        skill.getName(),
                        skill.getType(),
                        skill.getCreatedAt(),
                        skill.isDeleted()
                )
        ).toList();
    }
}
