package com.RDS.skilltree.models;

import com.RDS.skilltree.enums.SkillTypeEnum;
import com.RDS.skilltree.utils.TrackedProperties;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
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

//    @ManyToOne
//    @JoinColumn(name = "created_by", nullable = false)
//    private RdsUserViewModel createdBy;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

//    @ManyToOne
//    @JoinColumn(name = "updated_by")
//    private RdsUserViewModel updatedBy;

    @Column(name = "updated_by")
    private String updateBy;
}
