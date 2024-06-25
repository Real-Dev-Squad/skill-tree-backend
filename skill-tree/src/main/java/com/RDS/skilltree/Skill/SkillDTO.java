package com.RDS.skilltree.Skill;

import com.RDS.skilltree.User.UserDTO;
import com.RDS.skilltree.models.Skill;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillDTO {
    private Integer id;
    private SkillTypeEnum type;
    private String name;
    private Set<UserDTO> users;

    public static SkillDTO toDto(Skill skill) {
        return SkillDTO.builder().id(skill.getId()).name(skill.getName()).type(skill.getType()).build();
    }

    public static SkillDTO getSkillsWithUsers(Skill skill) {
        Set<UserDTO> users = new HashSet<>();

        return SkillDTO.builder()
                .id(skill.getId())
                .name(skill.getName())
                .type(skill.getType())
                .users(users)
                .build();
    }
}
