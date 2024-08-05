package com.RDS.skilltree.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEndorsementRequestDto {
    @NotNull(message = "Message cannot be empty")
    private String message;

    @NotNull(message = "user id cannot be null")
    private String endorseId;
}
