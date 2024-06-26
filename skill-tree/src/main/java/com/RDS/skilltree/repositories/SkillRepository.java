package com.RDS.skilltree.repositories;

import com.RDS.skilltree.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    boolean existsByName(String name);
}