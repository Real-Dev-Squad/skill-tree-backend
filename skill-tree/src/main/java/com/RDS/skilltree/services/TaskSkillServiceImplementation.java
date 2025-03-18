package com.RDS.skilltree.services;

import com.RDS.skilltree.exceptions.SkillNotFoundException;
import com.RDS.skilltree.exceptions.TaskSkillAssociationAlreadyExistsException;
import com.RDS.skilltree.models.TaskSkill;
import com.RDS.skilltree.models.TaskSkillId;
import com.RDS.skilltree.repositories.TaskSkillRepository;
import com.RDS.skilltree.repositories.SkillRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskSkillServiceImplementation implements TaskSkillService {

    private final TaskSkillRepository taskSkillRepository;
    private final SkillRepository skillRepository;

    public TaskSkillServiceImplementation(TaskSkillRepository taskSkillRepository, SkillRepository skillRepository) {
        this.taskSkillRepository = taskSkillRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    @Transactional
    public void createTaskSkills(String taskId, List<Integer> skillIds, String createdBy) {
        LocalDateTime now = LocalDateTime.now();
        for (Integer skillId : skillIds) {
            // Check if the skill exists; if not, throw SkillNotFoundException.
            if (!skillRepository.existsById(skillId)) {
                throw new SkillNotFoundException("Skill not found for skillId = " + skillId);
            }
            TaskSkill taskSkill = TaskSkill.builder()
                    .id(new TaskSkillId(taskId, skillId))
                    .createdAt(now)
                    .createdBy(createdBy)
                    .build();
            try {
                taskSkillRepository.save(taskSkill);
            } catch (DataIntegrityViolationException e) {
                throw new TaskSkillAssociationAlreadyExistsException(
                        "Task-Skill association already exists for task " + taskId + " and skill " + skillId, e);
            }
        }
    }
}
