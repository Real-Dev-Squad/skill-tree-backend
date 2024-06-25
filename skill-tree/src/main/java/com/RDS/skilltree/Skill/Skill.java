package com.RDS.skilltree.Skill;

import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.utils.TrackedProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Table(name = "skills")
public class Skill extends TrackedProperties {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "skill_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SkillTypeEnum type = SkillTypeEnum.ATOMIC;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserModel createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserModel updatedBy;
}
