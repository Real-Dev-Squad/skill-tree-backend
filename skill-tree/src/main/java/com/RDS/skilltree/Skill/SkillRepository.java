package com.RDS.skilltree.Skill;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<SkillModel, UUID> {
    Optional<SkillModel> findByName(String name);

    Page<SkillModel> findAll(Pageable pageable);
}
