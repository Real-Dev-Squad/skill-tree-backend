package com.RDS.skilltree.aspects;

import com.RDS.skilltree.annotations.AuthorizedRoles;
import com.RDS.skilltree.enums.UserRoleEnum;
import com.RDS.skilltree.exceptions.ForbiddenException;
import com.RDS.skilltree.models.JwtUser;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizedRolesAspect {

    @Around("@within(authorizedRoles) || @annotation(authorizedRoles)")
    public Object authorize(ProceedingJoinPoint joinPoint, AuthorizedRoles authorizedRoles)
            throws Throwable {
        JwtUser jwtDetails =
                (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserRoleEnum role = jwtDetails.getRole();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();

        AuthorizedRoles methodAuthorized = method.getAnnotation(AuthorizedRoles.class);
        AuthorizedRoles classAuthorized = targetClass.getAnnotation(AuthorizedRoles.class);

        UserRoleEnum[] allowedRoles = {};

        if (methodAuthorized != null) {
            allowedRoles = methodAuthorized.value();
        } else if (classAuthorized != null) {
            allowedRoles = classAuthorized.value();
        } else {
            // If no roles are specified, proceed with the method execution
            joinPoint.proceed();
        }

        if (!isAuthorized(role, allowedRoles)) {
            throw new ForbiddenException("You're not authorized to make this request");
        }

        return joinPoint.proceed();
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
