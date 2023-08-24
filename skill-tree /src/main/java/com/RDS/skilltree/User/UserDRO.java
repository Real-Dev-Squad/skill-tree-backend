package com.RDS.skilltree.User;

import com.RDS.skilltree.Skill.SkillModel;
import lombok.*;

import java.net.URL;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserDRO {

    private String firstName;

    private String lastName;

    private URL imageUrl;

    private UserType type;

    private UserRole role;

    private Set<SkillModel> skills;

    public static UserModel toModel(UserDRO user) {
        return new UserModel(
                user.getFirstName(),
                user.getLastName(),
                user.getImageUrl(),
                user.getType(),
                user.getRole());
    }
}
