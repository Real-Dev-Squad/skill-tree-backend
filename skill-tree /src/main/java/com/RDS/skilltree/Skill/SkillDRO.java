package com.RDS.skilltree.Skill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
