package com.RDS.skilltree.EndorsementList;

import com.RDS.skilltree.Endorsement.EndorsementModel;
import com.RDS.skilltree.User.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@Table(name = "endorsement_list")
public class EndorsementListModel {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @JoinColumn(name = "endorsement_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = EndorsementModel.class, cascade = CascadeType.ALL)
    private EndorsementModel endorsement;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = UserModel.class, cascade = CascadeType.ALL)
    private UserModel endorser;

    @Column(name = "description")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @Column(name = "type", nullable = false)
    private EndorsementType type;

    @Column(name = "created_at")
    private Instant createdAt;

    @JoinColumn(name = "created_by")
    @ManyToOne
    private UserModel createdBy;

    @Column(name="updated_at")
    private Instant updatedAt;

    @JoinColumn(name="updated_by")
    @ManyToOne
    private UserModel updatedBy;

    public EndorsementListModel(EndorsementModel endorsement, UserModel endorser, String description,
            EndorsementType type, UserModel createdBy) {
        this.endorsement = endorsement;
        this.endorser = endorser;
        this.description = description;
        this.type = type;
        this.deleted = false;
        this.createdBy = createdBy;
    }
}
