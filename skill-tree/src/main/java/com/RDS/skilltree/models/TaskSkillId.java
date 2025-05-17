package com.RDS.skilltree.models;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
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
    @Serial private static final long serialVersionUID = 1L;

    @NotNull(message = "Task ID cannot be null")
    private String taskId;

    @NotNull(message = "Skill ID cannot be null")
    private Integer skillId;
}
