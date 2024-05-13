package com.RDS.skilltree.Endorsement;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndorsementRepository extends JpaRepository<EndorsementModel, UUID> {
    List<EndorsementModel> findByEndorseeId(UUID userId);

    List<EndorsementModel> findBySkillId(UUID skillId);
}
