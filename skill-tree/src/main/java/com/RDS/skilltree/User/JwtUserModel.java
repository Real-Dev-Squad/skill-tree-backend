package com.RDS.skilltree.User;

import lombok.Getter;

@Getter
public class JwtUserModel {
    private final String rdsUserId;
    private final UserRoleEnum role;

    public JwtUserModel(String rdsUserId, UserRoleEnum role) {
        this.role = role;
        this.rdsUserId = rdsUserId;
    }
}
