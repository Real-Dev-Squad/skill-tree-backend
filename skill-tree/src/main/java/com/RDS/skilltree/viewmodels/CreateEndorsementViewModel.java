package com.RDS.skilltree.viewmodels;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEndorsementViewModel {
    @NotNull(message = "Message cannot be empty")
    private String message;

    @NotNull(message = "user id cannot be null")
    private String endorseId;

    @NotNull(message = "skill id cannot be null")
    private Integer skillId;
}
