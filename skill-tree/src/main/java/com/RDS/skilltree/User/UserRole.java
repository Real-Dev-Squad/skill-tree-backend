package com.RDS.skilltree.User;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

public enum UserRole  {
    USER("user"),
    MEMBER("member"),
    SUPERUSER("super_user"),
    GUEST("guest");

    public final String label;

     UserRole(String label) {
        this.label = label;
    }
    public static UserRole fromString(String text) {
        for (  UserRole role : UserRole.values()) {
            if (role.label.equalsIgnoreCase(text)) {
                return role;
            }
        }
        return UserRole.GUEST;
    }
    public static String[] getAllRoles(){
        return  Arrays.stream(UserRole.values()).map(UserRole::name).toArray(String[]::new);
    }

}
