package com.RDS.skilltree.Skill;

import com.RDS.skilltree.User.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillDTO {
    private UUID id;
    private SkillType type;
    private String name;
    private Set<UserModel> users;

    public static SkillDTO toDto(SkillModel skillModel) {
        return SkillDTO.builder()
                .id(skillModel.getId())
                .name(skillModel.getName())
                .type(skillModel.getType())
                .users(skillModel.getUsers())
                .build();
    }
}
