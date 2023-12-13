package com.RDS.skilltree.Skill;

import com.RDS.skilltree.User.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillDTO {
    private UUID id;
    private SkillType type;
    private String name;
    private Set<UserDTO> users;

    public static SkillDTO toDto(SkillModel skillModel) {
        return SkillDTO.builder()
                .id(skillModel.getId())
                .name(skillModel.getName())
                .type(skillModel.getType())
                .build();
    }

    public static SkillDTO getSkillsWithUsers(SkillModel skillModel) {
        Set<UserDTO> users = skillModel.getUsers()
                .stream()
                .map(UserDTO::toDTO)
                .collect(Collectors.toSet());

        return SkillDTO.builder()
                .id(skillModel.getId())
                .name(skillModel.getName())
                .type(skillModel.getType())
                .users(users)
                .build();
    }
}
