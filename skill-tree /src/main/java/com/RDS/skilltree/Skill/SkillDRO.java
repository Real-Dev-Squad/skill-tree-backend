package com.RDS.skilltree.Skill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillDRO {
    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "SkillType cannot be null")
    private SkillType type;

    @NotNull(message = "Created by user Id cannot be null")
    private UUID createdBy;


    public static SkillModel toModel(SkillDRO skillDRO) {
        return SkillModel.builder()
                .name(skillDRO.getName())
                .type(skillDRO.getType())
                .deleted(false)
                .build();
    }
}
