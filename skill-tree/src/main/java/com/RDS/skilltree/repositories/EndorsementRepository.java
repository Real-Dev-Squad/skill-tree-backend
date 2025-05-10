package com.RDS.skilltree.repositories;

import com.RDS.skilltree.models.Endorsement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EndorsementRepository extends JpaRepository<Endorsement, Integer> {
    List<Endorsement> findBySkillId(Integer skillId);

    List<Endorsement> findByEndorseIdAndSkillId(String endorseId, Integer skillId);

    @Query("""
            SELECT EXISTS
            (SELECT 1
            FROM Endorsement e JOIN UserSkills us ON (e.skill.id = us.skill.id and e.endorseId = us.userId)
            WHERE (e.endorserId = :endorserId and us.userId = :endorseId and us.skill.id = :skillId))
            """)
    boolean existsByEndorseIdAndEndorserIdAndSkillId(
            @Param("endorseId") String endorseId,
            @Param("endorserId") String endorserId,
            @Param("skillId") Integer skillId);
}
