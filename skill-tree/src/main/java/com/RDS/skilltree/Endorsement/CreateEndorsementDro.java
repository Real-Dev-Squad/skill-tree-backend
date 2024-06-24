package com.RDS.skilltree.Endorsement;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateEndorsementDro {
    @NotNull(message = "Message cannot be empty")
    private String message;

    @NotNull(message = "user id cannot be null")
    private String endorseId;

    @NotNull(message = "skill id cannot be null")
    private Integer skillId;
}
