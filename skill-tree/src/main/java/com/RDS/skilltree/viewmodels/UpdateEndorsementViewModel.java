package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.utils.Constants.ExceptionMessages;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEndorsementViewModel {
    @NotNull(message = ExceptionMessages.ENDORSEMENT_MESSAGE_EMPTY)
    private String message;
}
