package com.RDS.skilltree.utils;

import com.RDS.skilltree.Exceptions.InvalidParameterException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.validator.internal.constraintvalidators.hv.UUIDValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Middleware to intercept request and perform UUID validation
 */
@Component
public class UUIDValidationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UUIDValidationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String skillIdString = request.getParameter("skillID");
        String userIdString = request.getParameter("userID");

        if (skillIdString != null && !skillIdString.isEmpty() && !isValidUUID(skillIdString)) {
            throw new InvalidParameterException("skillID","Invalid UUID format");
        }

        if (userIdString != null && !userIdString.isEmpty() && !isValidUUID(userIdString)) {
            throw new InvalidParameterException("userID","Invalid UUID format");
        }

        return true;
    }

    private boolean isValidUUID(String uuidString) {
        Pattern UUID_REGEX =
                Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        return UUID_REGEX.matcher(uuidString).matches();

    }
}
