package com.RDS.skilltree.models;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class TaskSkillId implements Serializable {
    private String taskId;
    private Integer skillId;
}
