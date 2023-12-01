package com.RDS.skilltree.EndorsementList;

import com.RDS.skilltree.Endorsement.EndorsementModel;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.utils.TrackedProperties;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@Table(name = "endorsement_list")
public class EndorsementListModel extends TrackedProperties {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @JoinColumn(name = "endorsement_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = EndorsementModel.class, cascade = CascadeType.ALL)
    @JsonBackReference
    private EndorsementModel endorsement;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(targetEntity = UserModel.class, cascade = CascadeType.ALL)
    private UserModel endorser;

    @Column(name = "description")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EndorsementType type;

    public EndorsementListModel(EndorsementModel endorsement, UserModel endorser, String description,
            EndorsementType type) {
        this.endorsement = endorsement;
        this.endorser = endorser;
        this.description = description;
        this.type = type;
        this.deleted = false;
    }
}
