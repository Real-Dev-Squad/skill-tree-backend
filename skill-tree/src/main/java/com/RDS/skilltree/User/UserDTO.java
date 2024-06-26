package com.RDS.skilltree.User;

import com.RDS.skilltree.models.Skill;
import java.net.URL;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {

    private String id;

    private String rdsUserId;

    private String firstName;

    private String lastName;

    private URL imageUrl;

    private UserRoleEnum role;

    private Set<Skill> skills;

    public static UserDTO toDTO(UserModel user) {

        return UserDTO.builder().id(user.getId()).rdsUserId(user.getRdsUserId()).build();
    }

    public static UserDTO getUsersWithSkills(UserModel user) {
        //        Set<SkillDTO> skills = []
        //                user.getSkills().stream().map(SkillDTO::toDto).collect(Collectors.toSet());

        return UserDTO.builder()
                .id(user.getId())
                .rdsUserId(user.getRdsUserId())
                //                .skills(skills)
                //                .role(user.getRole())
                .build();
    }
}
