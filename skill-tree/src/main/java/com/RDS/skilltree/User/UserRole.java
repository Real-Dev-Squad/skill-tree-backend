package com.RDS.skilltree.User;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public enum UserRole  {
    USER("user"),
    MEMBER("member"),
    SUPERUSER("super_user"),
    MAVEN("maven");

    public final String label;

     UserRole(String label) {
        this.label = label;
    }
    public static UserRole fromString(String text) {
        for (  UserRole b : UserRole.values()) {
            if (b.label.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}
