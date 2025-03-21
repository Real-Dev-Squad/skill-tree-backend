package com.RDS.skilltree.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.*;

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
