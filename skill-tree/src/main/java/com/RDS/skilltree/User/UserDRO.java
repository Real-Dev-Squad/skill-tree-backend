package com.RDS.skilltree.User;

import java.net.URL;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDRO {
    private String rdsUserId;

    private String firstName;

    private String lastName;

    private URL imageUrl;

    private UserRoleEnum role;

    public static UserModel toModel(UserDRO user) {
        return UserModel.builder()
                .rdsUserId(user.getRdsUserId())
                //                .role(user.getRole())
                .build();
    }

    public static UserDRO fromModel(UserModel user) {
        return UserDRO.builder()
                .rdsUserId(user.getRdsUserId())
                //                .role(user.getRole())
                .build();
    }

    public static UserModel compareAndUpdateModel(UserModel user, UserDRO userDRO) {
        if (userDRO.getRdsUserId() != null) {
            user.setRdsUserId(user.getRdsUserId());
        }
        //        if (userDRO.getRole() != null) {
        //            user.setRole(user.getRole());
        //        }
        user.setUpdatedAt(Instant.now());
        return user;
    }
}
