package com.RDS.skilltree.services;

import com.RDS.skilltree.exceptions.SkillNotFoundException;
import com.RDS.skilltree.exceptions.TaskSkillAssociationAlreadyExistsException;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.models.TaskSkill;
import com.RDS.skilltree.models.TaskSkillId;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.TaskSkillRepository;
import com.RDS.skilltree.viewmodels.CreateTaskSkillViewModel;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    public CreateTaskSkillViewModel createTaskSkills(
            String taskId, List<Integer> skillIds, String createdBy) {
        LocalDateTime now = LocalDateTime.now();
        // Remove duplicate skill IDs
        Set<Integer> uniqueSkillIds = new HashSet<>(skillIds);

        List<Skill> skills = skillRepository.findAllById(uniqueSkillIds);
        // Verify if any requested skill ID is missing
        if (skills.size() != uniqueSkillIds.size()) {
            // Determine missing IDs
            Set<Integer> foundIds = skills.stream().map(Skill::getId).collect(Collectors.toSet());
            uniqueSkillIds.removeAll(foundIds);
            throw new SkillNotFoundException("Skill not found for skillId(s): " + uniqueSkillIds);
        }
        for (Integer skillId : uniqueSkillIds) {
            TaskSkillId tsId = new TaskSkillId(taskId, skillId);

            if (taskSkillRepository.existsById(tsId)) {
                throw new TaskSkillAssociationAlreadyExistsException(
                        "Task-Skill association already exists for task " + taskId + " and skill " + skillId);
            }

            // Find the corresponding Skill object from the fetched list
            Skill skill =
                    skills.stream()
                            .filter(s -> s.getId().equals(skillId))
                            .findFirst()
                            .orElseThrow(
                                    () -> new SkillNotFoundException("Skill not found for skillId = " + skillId));

            TaskSkill taskSkill =
                    TaskSkill.builder()
                            .id(tsId)
                            .skill(skill) // Set the Skill relationship
                            .createdAt(now)
                            .createdBy(createdBy)
                            .build();
            taskSkillRepository.saveAndFlush(taskSkill);
        }
        return new CreateTaskSkillViewModel("Skills are linked to task successfully!");
    }
}
