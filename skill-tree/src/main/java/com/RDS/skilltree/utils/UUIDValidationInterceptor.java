package com.RDS.skilltree.utils;

import com.RDS.skilltree.Exceptions.InvalidParameterException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UUIDValidationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UUIDValidationInterceptor.class);
    private static final Pattern UUID_REGEX =
            Pattern.compile(
                    "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) {
        String skillID = request.getParameter("skillID");
        String userID = request.getParameter("userID");

        if (skillID != null && !skillID.isEmpty() && !isValidUUID(skillID)) {
            throw new InvalidParameterException("skillID", "Invalid UUID format");
        }

        if (userID != null && !userID.isEmpty() && !isValidUUID(userID)) {
            throw new InvalidParameterException("userID", "Invalid UUID format");
        }

        return true;
    }

    private boolean isValidUUID(String uuidString) {

        return UUID_REGEX.matcher(uuidString).matches();
    }
}
