package com.RDS.skilltree.Skill;

import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.utils.TrackedProperties;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@Getter
@Table(name = "Skill")
public class SkillModel extends TrackedProperties {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "skill_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SkillType type = SkillType.ATOMIC;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @JsonBackReference
    @JsonIgnore
    @ManyToMany(mappedBy = "skills", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserModel> users;

}
