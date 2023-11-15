package com.RDS.skilltree.Skill;

import com.RDS.skilltree.User.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
