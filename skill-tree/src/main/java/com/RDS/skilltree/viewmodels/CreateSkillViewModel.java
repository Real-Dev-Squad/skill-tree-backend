package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.Skill.SkillTypeEnum;
import com.RDS.skilltree.User.UserModel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSkillViewModel {
    @NotNull(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "SkillType cannot be empty")
    private SkillTypeEnum type;

    private UserModel createdBy;
}
