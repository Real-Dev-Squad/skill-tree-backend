package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.User.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@Table(name = "endorsements")
public class EndorsementModel {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(targetEntity = UserModel.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;

    @ManyToOne(targetEntity = SkillModel.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    private SkillModel skill;

    @Column(name = "endorsement_status")
    private EndorsementStatus status;

    @Column(name = "created_at")
    @GeneratedValue
    private Instant createdAt;

    @JoinColumn(name = "created_by")
    @ManyToOne
    private UserModel createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @JoinColumn(name = "updated_by")
    @ManyToOne
    private UserModel updatedBy;

    public EndorsementModel(UserModel user, SkillModel skill) {
        this.status = EndorsementStatus.PENDING;
        this.user = user;
        this.skill = skill;
    }
}
