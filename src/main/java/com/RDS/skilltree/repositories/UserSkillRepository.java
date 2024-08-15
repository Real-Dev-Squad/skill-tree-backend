package com.RDS.skilltree.repositories;

import com.RDS.skilltree.enums.UserSkillStatusEnum;
import com.RDS.skilltree.models.UserSkills;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkillRepository extends JpaRepository<UserSkills, Integer> {
    List<UserSkills> findByStatus(UserSkillStatusEnum status);

    List<UserSkills> findByUserIdAndSkillId(String userId, Integer skillId);
}
