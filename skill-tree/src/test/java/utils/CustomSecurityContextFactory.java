package utils;

import com.RDS.skilltree.enums.UserRoleEnum;
import com.RDS.skilltree.models.JwtUser;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class CustomSecurityContextFactory
        implements WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser customMockUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        List<UserRoleEnum> roles =
                Arrays.stream(customMockUser.authorities()).map(UserRoleEnum::valueOf).toList();
        UserRoleEnum mainRole = roles.isEmpty() ? UserRoleEnum.USER : roles.get(0);

        // Create jwt user
        JwtUser jwtUser = new JwtUser(customMockUser.username(), mainRole);

        // Map roles to Spring Security authorities
        List<SimpleGrantedAuthority> authorities =
                roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList());

        // Set JwtUser as the principal in Authentication
        Authentication auth = new UsernamePasswordAuthenticationToken(jwtUser, null, authorities);
        context.setAuthentication(auth);
        return context;
    }
}
