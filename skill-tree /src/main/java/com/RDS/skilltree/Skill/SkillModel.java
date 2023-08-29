package com.RDS.skilltree.Skill;

import com.RDS.skilltree.User.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@Table(name = "Skill")
public class SkillModel {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "skill_type", nullable = false)
    private SkillType type;

    @Column(name = "created_at")
    @GeneratedValue
    private Instant createdAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @JoinColumn(name = "created_by")
    @ManyToOne
    private UserModel createdBy;

    @Column(name="updated_at")
    private Instant updatedAt;

    @JoinColumn(name="updated_by")
    @ManyToOne
    private UserModel updatedBy;

    @JsonBackReference
    @JsonIgnore
    @ManyToMany(mappedBy = "skills", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserModel> users;

    public SkillModel(String name, SkillType type) {
        this.name = name;
        this.type = type;
        this.deleted = false;
    }
}
