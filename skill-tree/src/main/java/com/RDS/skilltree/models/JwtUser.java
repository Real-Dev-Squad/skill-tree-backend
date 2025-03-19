package com.RDS.skilltree.models;

import com.RDS.skilltree.enums.UserRoleEnum;
import lombok.Getter;

@Getter
public class JwtUser {
    private final String rdsUserId;
    private final UserRoleEnum role;

    public JwtUser(String rdsUserId, UserRoleEnum role) {
        this.role = role;
        this.rdsUserId = rdsUserId;
    }
}
