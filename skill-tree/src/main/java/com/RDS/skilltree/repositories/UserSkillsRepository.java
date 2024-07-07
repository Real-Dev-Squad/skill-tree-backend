package com.RDS.skilltree.repositories;

import com.RDS.skilltree.User.UserSkillStatusEnum;
import com.RDS.skilltree.User.UserSkillsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserSkillsRepository extends JpaRepository<UserSkillsModel, Integer> {
    Page<UserSkillsModel> findAllByStatus(@Param("status") UserSkillStatusEnum status, Pageable pageable);

//    Optional<UserSkillsModel> findByUserIdAndSkillId(@Param("userId") Integer userId, @Param("skillId") Integer skillId);
}
