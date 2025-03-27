package com.RDS.skilltree.services;

import com.RDS.skilltree.exceptions.SkillNotFoundException;
import com.RDS.skilltree.exceptions.TaskSkillAssociationAlreadyExistsException;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.models.TaskSkill;
import com.RDS.skilltree.models.TaskSkillId;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.TaskSkillRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskSkillServiceImplementation implements TaskSkillService {

    private final TaskSkillRepository taskSkillRepository;
    private final SkillRepository skillRepository;

    public TaskSkillServiceImplementation(
            TaskSkillRepository taskSkillRepository, SkillRepository skillRepository) {
        this.taskSkillRepository = taskSkillRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    @Transactional
    public void createTaskSkills(String taskId, List<Integer> skillIds, String createdBy) {
        LocalDateTime now = LocalDateTime.now();
        // Remove duplicate skill IDs
        Set<Integer> uniqueSkillIds = new HashSet<>(skillIds);
        for (Integer skillId : uniqueSkillIds) {
            // Fetch the skill entity before using it in TaskSkill.
            Skill skill =
                    skillRepository
                            .findById(skillId)
                            .orElseThrow(
                                    () -> new SkillNotFoundException("Skill not found for skillId = " + skillId));

            // Create a composite key for the association.
            TaskSkillId tsId = new TaskSkillId(taskId, skillId);
            // Explicitly check if an association already exists.
            if (taskSkillRepository.existsById(tsId)) {
                throw new TaskSkillAssociationAlreadyExistsException(
                        "Task-Skill association already exists for task " + taskId + " and skill " + skillId);
            }
            // Create and save the new association.
            TaskSkill taskSkill =
                    TaskSkill.builder().id(tsId).skill(skill).createdAt(now).createdBy(createdBy).build();
            taskSkillRepository.saveAndFlush(taskSkill);
        }
    }
}
