package com.RDS.skilltree.utils;

import com.RDS.skilltree.Exceptions.InvalidParameterException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UUIDValidationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UUIDValidationInterceptor.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) {
        String skillID = request.getParameter("skillID");
        String userID = request.getParameter("userID");

        if (skillID != null && !skillID.isEmpty() && !CommonUtils.isValidUUID(skillID)) {
            throw new InvalidParameterException("skillID", "Invalid UUID format");
        }

        if (userID != null && !userID.isEmpty() && !CommonUtils.isValidUUID(userID)) {
            throw new InvalidParameterException("userID", "Invalid UUID format");
        }

        return true;
    }
}
