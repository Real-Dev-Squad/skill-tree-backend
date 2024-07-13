package com.RDS.skilltree.repositories;

import com.RDS.skilltree.User.UserSkillStatusEnum;
import com.RDS.skilltree.User.UserSkillsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSkillRepository extends JpaRepository<UserSkillsModel, Integer> {
    List<UserSkillsModel> findByStatus(UserSkillStatusEnum status);

    List<UserSkillsModel> findByUserIdAndSkillId(String userId, Integer skillId);
}
