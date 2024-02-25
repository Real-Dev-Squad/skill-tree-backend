package com.RDS.skilltree.Filters;

import com.RDS.skilltree.Authentication.UserAuthenticationToken;
import com.RDS.skilltree.User.*;
import com.RDS.skilltree.utils.FetchAPI;
import com.RDS.skilltree.utils.JWTUtils;
import com.RDS.skilltree.utils.RDSUser.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Value("${cookieName}")
    private String cookieName;
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private FetchAPI fetchAPI;


    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain)
            throws ServletException, IOException {

        String token = getJWTFromRequest(request);

        try {
            if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                String rdsUserId   = jwtUtils.getRDSUserId(token);
                String role= jwtUtils.getUserRole(token);


            UserAuthenticationToken authentication = new UserAuthenticationToken(role, rdsUserId);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception e) {
            log.error("Error in fetching the user details, error : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        filterChain.doFilter(request, response);
    }

    public String getJWTFromRequest(HttpServletRequest request) {

        /*  */
        Cookie RDScookie = WebUtils.getCookie(request, cookieName);
        if(RDScookie != null)
            return RDScookie.getValue();

        /*  */
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
