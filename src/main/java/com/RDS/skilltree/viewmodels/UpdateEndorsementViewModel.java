package com.RDS.skilltree.viewmodels;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEndorsementViewModel {
    @NotNull(message = "Message cannot be empty")
    private String message;
}
