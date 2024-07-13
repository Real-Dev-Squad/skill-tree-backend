package com.RDS.skilltree.repositories;

import com.RDS.skilltree.models.Endorsement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndorsementRepository extends JpaRepository<Endorsement, Integer> {
    List<Endorsement> findBySkillId(Integer skillId);
}
