package com.RDS.skilltree.User;

import com.RDS.skilltree.Skill.SkillModel;
import lombok.*;
import org.hibernate.usertype.UserType;

import java.net.URL;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class UserDTO {

    private UUID id;

    private String rdsUserId;

    private String firstName;

    private String lastName;

    private URL imageUrl;

    private UserType type;

    private UserRole role;

    private Set<SkillModel> skills;

    public static UserDTO toDTO(UserModel user) {
        return UserDTO.builder()
                .rdsUserId(user.getRdsUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .imageUrl(user.getImageUrl())
                .role(user.getRole())
                .skills(user.getSkills())
                .build();
    }
}
