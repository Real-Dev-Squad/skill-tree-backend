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
import java.util.Map;
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
        // Remove duplicate skill IDs
        Set<Integer> uniqueSkillIds = new HashSet<>(skillIds);

        // FIRST: Check if any TaskSkill associations already exist (batch check)
        Set<Integer> existingSkillIds =
                taskSkillRepository.findSkillIdsByTaskIdAndSkillIdIn(taskId, uniqueSkillIds);
        if (!existingSkillIds.isEmpty()) {
            throw new TaskSkillAssociationAlreadyExistsException(
                    "Task-Skill associations already exist for task "
                            + taskId
                            + " and skills: "
                            + existingSkillIds);
        }

        // SECOND: Load all skills at once and verify they exist
        List<Skill> skills = skillRepository.findAllById(uniqueSkillIds);

        // Check for missing skills
        if (skills.size() != uniqueSkillIds.size()) {
            Set<Integer> foundSkillIds = skills.stream().map(Skill::getId).collect(Collectors.toSet());
            Set<Integer> missingIds = new HashSet<>(uniqueSkillIds);
            missingIds.removeAll(foundSkillIds);
            throw new SkillNotFoundException("Skill not found for skillId(s): " + missingIds);
        }

        // Create a map for quick lookups
        Map<Integer, Skill> skillsMap = skills.stream().collect(Collectors.toMap(Skill::getId, s -> s));

        // Create and save TaskSkill entities
        LocalDateTime now = LocalDateTime.now();
        List<TaskSkill> taskSkillsToSave =
                uniqueSkillIds.stream()
                        .map(
                                skillId ->
                                        TaskSkill.builder()
                                                .id(new TaskSkillId(taskId, skillId))
                                                .skill(skillsMap.get(skillId)) // Set the actual Skill entity reference
                                                .createdAt(now)
                                                .createdBy(createdBy)
                                                .build())
                        .collect(Collectors.toList());

        taskSkillRepository.saveAll(taskSkillsToSave);

        return new CreateTaskSkillViewModel("Skills are linked to task successfully!");
    }
}
