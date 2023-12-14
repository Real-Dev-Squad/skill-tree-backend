package com.RDS.skilltree.User;

import com.RDS.skilltree.Skill.SkillDTO;
import lombok.Builder;
import lombok.Getter;

import java.net.URL;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class UserDTO {

    private UUID id;

    private String rdsUserId;

    private String firstName;

    private String lastName;

    private URL imageUrl;

    private UserRole role;

    private Set<SkillDTO> skills;

    public static UserDTO toDTO(UserModel user) {

        return UserDTO.builder()
                .id(user.getId())
                .rdsUserId(user.getRdsUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .imageUrl(user.getImageUrl())
                .role(user.getRole())
                .build();
    }

    public static UserDTO getUsersWithSkills(UserModel user){
        Set<SkillDTO> skills = user.getSkills()
                .stream()
                .map(SkillDTO::toDto)
                .collect(Collectors.toSet());

        return UserDTO.builder()
                .id(user.getId())
                .rdsUserId(user.getRdsUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .imageUrl(user.getImageUrl())
                .skills(skills)
                .role(user.getRole())
                .build();
    }
}
