package com.RDS.skilltree.Endorsement;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndorsementRepository extends JpaRepository<EndorsementModel, UUID> {
    Page<EndorsementModel> findBySkillId(Integer skillId, Pageable pageable);
}
