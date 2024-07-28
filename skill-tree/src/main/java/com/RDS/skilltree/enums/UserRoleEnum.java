package com.RDS.skilltree.enums;

import java.util.Arrays;

public enum UserRoleEnum {
    USER("user"),
    MEMBER("member"),
    SUPERUSER("super_user"),
    GUEST("guest");

    public final String label;

    UserRoleEnum(String label) {
        this.label = label;
    }

    public static UserRoleEnum fromString(String text) {
        for (UserRoleEnum role : UserRoleEnum.values()) {
            if (role.label.equalsIgnoreCase(text)) {
                return role;
            }
        }
        return UserRoleEnum.GUEST;
    }

    public static String[] getAllRoles() {
        return Arrays.stream(UserRoleEnum.values()).map(UserRoleEnum::name).toArray(String[]::new);
    }
}
