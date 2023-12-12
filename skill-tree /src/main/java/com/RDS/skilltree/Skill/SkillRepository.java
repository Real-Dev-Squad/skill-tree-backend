package com.RDS.skilltree.Skill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<SkillModel, UUID> {
    Optional<SkillModel> findByName(String name);
    Page<SkillModel> findAll(Pageable pageable);
}
