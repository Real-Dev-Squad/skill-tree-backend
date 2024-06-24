package com.RDS.skilltree.Skill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillDRO {
    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "SkillType cannot be null")
    private SkillType type;

    public static Skill toModel(SkillDRO skillDRO) {
        return Skill.builder()
                .name(skillDRO.getName())
                .type(skillDRO.getType())
                .isDeleted(false)
                .build();
    }
}
