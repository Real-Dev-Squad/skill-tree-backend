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

    private UserRole role;

    public static UserModel toModel(UserDRO user) {
        return UserModel.builder()
                .rdsUserId(user.getRdsUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .imageUrl(user.getImageUrl())
                .build();
    }

    public static UserDRO fromModel(UserModel user) {
        return UserDRO.builder()
                .rdsUserId(user.getRdsUserId())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .imageUrl(user.getImageUrl())
                .build();
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
        return user;
    }
}
