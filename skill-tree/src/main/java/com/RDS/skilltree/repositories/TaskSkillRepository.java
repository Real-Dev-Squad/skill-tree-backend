package com.RDS.skilltree.repositories;

import com.RDS.skilltree.models.TaskSkill;
import com.RDS.skilltree.models.TaskSkillId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskSkillRepository extends JpaRepository<TaskSkill, TaskSkillId> {
    List<TaskSkill> findByIdTaskId(String taskId);
}
