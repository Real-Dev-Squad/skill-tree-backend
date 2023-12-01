package com.RDS.skilltree.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDRO {
    private String rdsUserId;

    private String firstName;

    private String lastName;

    private URL imageUrl;

    private UserRole role;

    public static UserModel toModel(UserDRO user) {
        return new UserModel(
                user.getRdsUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getImageUrl(),
                user.getRole()
        );
    }

    public static UserModel compareAndUpdateModel(UserModel user, UserDRO userDRO) {
        if (userDRO.getRdsUserId() != null) {
            user.setRdsUserId(user.getRdsUserId());
        }
        if (userDRO.getFirstName() != null) {
            user.setFirstName(user.getFirstName());
        }
        if (userDRO.getLastName() != null) {
            user.setLastName(user.getLastName());
        }
        if (userDRO.getImageUrl() != null) {
            user.setImageUrl(user.getImageUrl());
        }
        if (userDRO.getRole() != null) {
            user.setRole(user.getRole());
        }
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(user);
        return user;
    }
}
