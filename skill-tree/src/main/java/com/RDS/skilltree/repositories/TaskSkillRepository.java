package com.RDS.skilltree.repositories;

import com.RDS.skilltree.models.TaskSkill;
import com.RDS.skilltree.models.TaskSkillId;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskSkillRepository extends JpaRepository<TaskSkill, TaskSkillId> {
    /**
     * Find skill IDs that already have associations with the given task ID
     */
    @Query(
            "SELECT ts.id.skillId FROM TaskSkill ts WHERE ts.id.taskId = :taskId AND ts.id.skillId IN :skillIds")
    Set<Integer> findSkillIdsByTaskIdAndSkillIdIn(
            @Param("taskId") String taskId, @Param("skillIds") Set<Integer> skillIds);
}
