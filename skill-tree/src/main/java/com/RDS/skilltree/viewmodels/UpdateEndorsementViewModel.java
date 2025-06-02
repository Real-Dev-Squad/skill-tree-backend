package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.utils.Constants.ExceptionMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEndorsementViewModel {
    @NotBlank(message = ExceptionMessages.ENDORSEMENT_MESSAGE_EMPTY)
    private String message;
}
