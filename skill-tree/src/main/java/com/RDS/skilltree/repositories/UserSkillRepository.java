package com.RDS.skilltree.repositories;

import com.RDS.skilltree.enums.UserSkillStatusEnum;
import com.RDS.skilltree.models.UserSkills;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSkillRepository extends JpaRepository<UserSkills, Integer> {
    List<UserSkills> findByStatus(UserSkillStatusEnum status);

    List<UserSkills> findByUserIdAndSkillId(String userId, Integer skillId);
}
