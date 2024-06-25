package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Skill.Skill;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.utils.TrackedProperties;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
@Table(name = "endorsements")
public class EndorsementModel extends TrackedProperties {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    private Skill skill;

    @ManyToOne
    @JoinColumn(name = "endorse_id", referencedColumnName = "id")
    private UserModel endorse;

    @ManyToOne(targetEntity = UserModel.class, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "endorser_id", referencedColumnName = "id")
    private UserModel endorser;

    @Column(name = "message", nullable = false)
    private String message;
}
