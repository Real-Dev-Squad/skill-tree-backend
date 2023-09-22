package com.RDS.skilltree.User;

import com.RDS.skilltree.Skill.SkillModel;
import lombok.*;

import java.net.URL;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
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
        return new UserDTO(user.getId(),
                user.getRdsUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getImageUrl(),
                user.getType(),
                user.getRole(),
                user.getSkills());
    }
}
