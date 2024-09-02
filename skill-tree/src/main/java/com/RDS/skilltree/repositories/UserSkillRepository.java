package com.RDS.skilltree.repositories;

import com.RDS.skilltree.enums.UserSkillStatusEnum;
import com.RDS.skilltree.models.UserSkills;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserSkillRepository extends JpaRepository<UserSkills, Integer> {
    List<UserSkills> findByStatus(UserSkillStatusEnum status);

    List<UserSkills> findByUserIdAndSkillId(String userId, Integer skillId);

    @Query(
            "SELECT us FROM UserSkills us "
                    + "JOIN Endorsement e ON us.userId = e.endorseId "
                    + "WHERE e.endorserId = :endorserId")
    List<UserSkills> findUserSkillsByEndorserId(@Param("endorserId") String endorserId);
}
