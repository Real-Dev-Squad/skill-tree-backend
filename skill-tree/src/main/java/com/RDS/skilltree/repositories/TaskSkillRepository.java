package com.RDS.skilltree.repositories;

import com.RDS.skilltree.models.TaskSkill;
import com.RDS.skilltree.models.TaskSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskSkillRepository extends JpaRepository<TaskSkill, TaskSkillId> {
    //find association by taskId from composite key
    List<TaskSkill> findByIdTaskId(String taskId);
}
