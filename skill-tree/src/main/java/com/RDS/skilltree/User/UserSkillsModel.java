package com.RDS.skilltree.User;

import com.RDS.skilltree.Skill.SkillModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_skills")
public class UserSkillsModel {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private SkillModel skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserSkillStatusEnum status = UserSkillStatusEnum.PENDING;
}
