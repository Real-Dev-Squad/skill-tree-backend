package com.RDS.skilltree.repositories;

import com.RDS.skilltree.User.UserSkillStatusEnum;
import com.RDS.skilltree.User.UserSkillsModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkillRepository extends JpaRepository<UserSkillsModel, Integer> {
    List<UserSkillsModel> findByStatus(UserSkillStatusEnum status);

    List<UserSkillsModel> findByUserIdAndSkillId(String userId, Integer skillId);
}
