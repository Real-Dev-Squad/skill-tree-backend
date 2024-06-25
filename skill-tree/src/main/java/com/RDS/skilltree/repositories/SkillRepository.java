package com.RDS.skilltree.repositories;

import com.RDS.skilltree.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    Optional<Skill> findByName(String name);

    boolean existsByName(String name);
}