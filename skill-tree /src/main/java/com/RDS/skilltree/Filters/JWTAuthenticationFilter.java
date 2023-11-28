package com.RDS.skilltree.Filters;

import com.RDS.skilltree.Authentication.UserAuthenticationToken;
import com.RDS.skilltree.User.*;
import com.RDS.skilltree.utils.FetchAPI;
import com.RDS.skilltree.utils.JWTUtils;
import com.RDS.skilltree.utils.RDSUser.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private FetchAPI fetchAPI;

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    public JWTAuthenticationFilter(UserService userService){
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getJWTFromRequest(request);
        try {
            if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                String rdsUserId = jwtUtils.getRDSUserId(token);


                UserModel userModel;

                if (userRepository.existsByRdsUserId(rdsUserId)) {
                    userModel = userRepository.findByRdsUserId(rdsUserId).get();
                } else {
                    CompletableFuture<Response> userResponse = fetchAPI.getRDSUserData(rdsUserId);
                    CompletableFuture.anyOf(userResponse).join();
                    Response rdsUserResponse = userResponse.get();
                    UserDRO userDRO = UserDRO.builder()
                            .rdsUserId(rdsUserId)
                            .firstName(rdsUserResponse.getUser().getFirst_name())
                            .lastName(rdsUserResponse.getUser().getLast_name())
                            .imageUrl(new URL(rdsUserResponse.getUser().getPicture().getUrl()))
                            .role(UserRole.USER)
                            .build();
                    userModel = UserDRO.toModel(userDRO);
                    userService.createUser(userDRO);
                }

                UserAuthenticationToken authentication = new UserAuthenticationToken(userModel);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
