package com.RDS.skilltree.Skill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillDRO {
    private String name;
    private SkillType type;
    private UUID createdBy;


    public static SkillModel toModel(SkillDRO skillDRO) {
        return SkillModel.builder()
                .name(skillDRO.getName())
                .type(skillDRO.getType())
                .deleted(false)
                .build();
    }
}
