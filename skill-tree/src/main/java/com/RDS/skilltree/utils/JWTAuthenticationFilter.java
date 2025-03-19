package com.RDS.skilltree.utils;

import com.RDS.skilltree.Authentication.UserAuthenticationToken;
import com.RDS.skilltree.config.SecurityConfig;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Value("${cookieName}")
    private String cookieName;

    @Autowired private JWTUtils jwtUtils;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getJWTFromRequest(request);

        if (StringUtils.hasText(token)) {
            Claims claims = jwtUtils.validateToken(token);
            String rdsUserId = jwtUtils.getRDSUserId(claims);
            String role = jwtUtils.getUserRole(claims);

            UserAuthenticationToken authentication = new UserAuthenticationToken(role, rdsUserId);

            authentication.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return SecurityConfig.NON_AUTH_ROUTES.stream().anyMatch(path::startsWith);
    }

    public String getJWTFromRequest(HttpServletRequest request) {

        /* check for cookie */
        Cookie RDScookie = WebUtils.getCookie(request, cookieName);
        if (RDScookie != null) return RDScookie.getValue();

        /* extract token from header */
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
