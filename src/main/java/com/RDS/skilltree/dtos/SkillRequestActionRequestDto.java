package com.RDS.skilltree.dtos;

import com.RDS.skilltree.enums.UserSkillStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillRequestActionRequestDto {
    @NotNull(message = "user id cannot be null")
    private String endorseId;

    @NotNull(message = "Action should not empty")
    private UserSkillStatusEnum action;
}
