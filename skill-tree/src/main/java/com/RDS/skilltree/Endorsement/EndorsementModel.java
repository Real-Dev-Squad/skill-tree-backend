package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Skill.Skill;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.utils.TrackedProperties;
import jakarta.persistence.*;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Table(name = "endorsements")
public class EndorsementModel extends TrackedProperties {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @ManyToOne(targetEntity = Skill.class, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    private Integer skillId;

    @ManyToOne(targetEntity = UserModel.class, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "endorse_id", referencedColumnName = "id")
    private String endorseId;

    @ManyToOne(targetEntity = UserModel.class, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "endorser_id", referencedColumnName = "id")
    private String endorserId;

    @Column(name = "message", nullable = false)
    private String message;
}
