package com.RDS.skilltree.services;

import com.RDS.skilltree.exceptions.SkillNotFoundException;
import com.RDS.skilltree.exceptions.TaskSkillAssociationAlreadyExistsException;
import com.RDS.skilltree.viewmodels.CreateTaskSkillViewModel;
import java.util.List;

public interface TaskSkillService {
    /**
     * Creates associations between a task and multiple skills.
     *
     * @param taskId    The unique identifier of the task.
     * @param skillIds  List of skill identifiers to associate with the task.
     * @param createdBy The identifier of the user creating these associations.
     * @return A response view model indicating success.
     * @throws TaskSkillAssociationAlreadyExistsException if an association already exists.
     * @throws SkillNotFoundException if any skill does not exist.
     */
    CreateTaskSkillViewModel createTaskSkills(
            String taskId, List<Integer> skillIds, String createdBy);
}
