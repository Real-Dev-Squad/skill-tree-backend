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

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


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
                    CompletableFuture.allOf(userResponse).join();
                    Response rdsUserResponse = userResponse.get();
                    UserDRO userDro = new UserDRO(rdsUserId, rdsUserResponse.getUser().getFirst_name(),
                            rdsUserResponse.getUser().getLast_name(),
                            new URL(rdsUserResponse.getUser().getPicture().getUrl()),
                            UserType.MEMBER, UserRole.USER);
                    userModel = UserDRO.toModel(userDro);
                    userService.createUser(userDro);
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
