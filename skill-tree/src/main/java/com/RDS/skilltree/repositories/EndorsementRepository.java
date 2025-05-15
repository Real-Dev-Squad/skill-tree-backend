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
            SELECT (COUNT(*) > 0) AS exists
                FROM Endorsement e
                WHERE e.endorserId = :endorserId
                  AND e.endorseId = :endorseId
                  AND e.skill.id = :skillId
            """)
    boolean existsByEndorseIdAndEndorserIdAndSkillId(
            @Param("endorseId") String endorseId,
            @Param("endorserId") String endorserId,
            @Param("skillId") Integer skillId);
}
