package com.RDS.skilltree.Endorsement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EndorsementRepository extends JpaRepository<EndorsementModel, UUID> {
    List<EndorsementModel> findByUserId(UUID userId);
    List<EndorsementModel> findBySkillId(UUID skillId);
}
