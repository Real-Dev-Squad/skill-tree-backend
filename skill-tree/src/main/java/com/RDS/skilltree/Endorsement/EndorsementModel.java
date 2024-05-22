package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.EndorsementList.EndorsementListModel;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.utils.TrackedProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Table(name = "endorsements")
public class EndorsementModel extends TrackedProperties {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "endorsee_id")
    private UUID endorseeId;

    @ManyToOne(targetEntity = SkillModel.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    private SkillModel skill;

    @OneToMany(mappedBy = "endorsement")
    @JsonManagedReference
    private List<EndorsementListModel> endorsersList = new ArrayList<>();

    @Column(name = "endorsement_status")
    @Enumerated(value = EnumType.STRING)
    private EndorsementStatus status = EndorsementStatus.PENDING;
}
