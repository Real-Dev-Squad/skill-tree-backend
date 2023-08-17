package com.RDS.skilltree.repository;

import com.RDS.skilltree.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
