package com.RDS.skilltree.services;

import com.RDS.skilltree.viewmodels.CreateTaskSkillViewModel;
import java.util.List;

public interface TaskSkillService {
    CreateTaskSkillViewModel createTaskSkills(
            String taskId, List<Integer> skillIds, String createdBy);
}
