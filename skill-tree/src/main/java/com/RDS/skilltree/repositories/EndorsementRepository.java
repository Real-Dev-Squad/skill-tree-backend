package com.RDS.skilltree.repositories;

import com.RDS.skilltree.models.Endorsement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndorsementRepository extends JpaRepository<Endorsement, Integer> {
    Page<Endorsement> findBySkillId(Integer skillId, Pageable pageable);

    List<Endorsement> getAllBySkillId(Integer skillId);
}
