package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.EndorsementList.EndorsementListModel;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.utils.TrackedProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@Table(name = "endorsements")
public class EndorsementModel extends TrackedProperties {
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

    @OneToMany(mappedBy = "endorsement")
    private List<EndorsementListModel> endorsersList = new ArrayList<>();

    @Column(name = "endorsement_status")
    @Enumerated(value = EnumType.STRING)
    private EndorsementStatus status = EndorsementStatus.PENDING;

    public EndorsementModel(UserModel user, SkillModel skill) {
        this.status = EndorsementStatus.PENDING;
        this.user = user;
        this.skill = skill;
    }
}
