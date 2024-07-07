package com.RDS.skilltree.aspects;

import com.RDS.skilltree.User.JwtUserModel;
import com.RDS.skilltree.User.UserRoleEnum;
import com.RDS.skilltree.annotations.AuthorizedRoles;
import com.RDS.skilltree.exceptions.ForbiddenException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizedRolesAspect {

    @Around("@annotation(authorizedRoles)")
    public Object authorize(ProceedingJoinPoint jointPoint, AuthorizedRoles authorizedRoles) throws Throwable {
        JwtUserModel jwtDetails =
                (JwtUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserRoleEnum role = jwtDetails.getRole();

        if (!isAuthorized(role, authorizedRoles.value())) {
            throw new ForbiddenException("You're not authorized to make this request");
        }

        return jointPoint.proceed();
    }

    private boolean isAuthorized(UserRoleEnum userRole, UserRoleEnum[] allowedRoles) {
        for (UserRoleEnum role : allowedRoles) {
            if (role.equals(userRole)) {
                return true;
            }
        }

        return false;
    }
}
