package com.RDS.skilltree.services;

import java.util.List;

public interface TaskSkillService {
    void createTaskSkills(String taskId, List<Integer> skillIds, String createdBy);
}
