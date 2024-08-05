package com.RDS.skilltree.annotations;

import com.RDS.skilltree.enums.UserRoleEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizedRoles {
    UserRoleEnum[] value() default {};
}
