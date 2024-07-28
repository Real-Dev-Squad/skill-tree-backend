package com.RDS.skilltree.models;

import com.RDS.skilltree.utils.TrackedProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "endorsements")
public class Endorsement extends TrackedProperties {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    private Skill skill;

    @Column(name = "endorse_id", nullable = false)
    private String endorseId;

    @Column(name = "endorser_id", nullable = false)
    private String endorserId;

    @Column(name = "message", nullable = false)
    private String message;
}
