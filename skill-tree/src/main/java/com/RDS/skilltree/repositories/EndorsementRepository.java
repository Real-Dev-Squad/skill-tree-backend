package com.RDS.skilltree.repositories;

import com.RDS.skilltree.models.Endorsement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndorsementRepository extends JpaRepository<Endorsement, Integer> {
    List<Endorsement> findBySkillId(Integer skillId);

    List<Endorsement> findByEndorseIdAndSkillId(String endorseId, Integer skillId);
}
