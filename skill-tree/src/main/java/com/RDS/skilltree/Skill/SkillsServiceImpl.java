package com.RDS.skilltree.Skill;

import com.RDS.skilltree.User.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SkillsServiceImpl implements SkillsService {
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    @Override
    public SkillDTO getSkillById(UUID id) {
        Optional<SkillModel> skillModel = skillRepository.findById(id);
        return skillModel.map(SkillDTO::getSkillsWithUsers).orElse(null);
    }

    @Override
    public SkillDTO getSkillByName(String skillName) {
        Optional<SkillModel> skillModel = skillRepository.findByName(skillName);
        return skillModel.map(SkillDTO::getSkillsWithUsers).orElse(null);
    }

    @Override
    public Page<SkillDTO> getAllSkills(Pageable pageable) {
        Page<SkillModel> skillModels = skillRepository.findAll(pageable);
        return skillModels.map(SkillDTO::getSkillsWithUsers);
    }

    @Override
    public SkillDTO createSkill(SkillDRO skillDRO) {
        SkillModel newSkill = SkillDRO.toModel(skillDRO);
        newSkill.setCreatedAt(Instant.now());
        newSkill.setUpdatedAt(Instant.now());

        try {
            skillRepository.save(newSkill);
        } catch (DataIntegrityViolationException ex) {
            log.error(
                    "Error saving the skills object with name : {}, with exception :{}",
                    skillDRO.getName(),
                    ex.getMessage(),
                    ex);
            throw ex;
        }
        return SkillDTO.toDto(newSkill);
    }
}
