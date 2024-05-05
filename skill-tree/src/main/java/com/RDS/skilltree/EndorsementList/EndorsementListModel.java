package com.RDS.skilltree.EndorsementList;

import com.RDS.skilltree.Endorsement.EndorsementModel;
import com.RDS.skilltree.utils.TrackedProperties;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    @Column(name = "endorser_id")
    private UUID endorserId;

    @Column(name = "description")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EndorsementType type;

    public EndorsementListModel(
            EndorsementModel endorsement, UUID endorserId, String description, EndorsementType type) {
        this.endorsement = endorsement;
        this.endorserId = endorserId;
        this.description = description;
        this.type = type;
        this.deleted = false;
    }
}
