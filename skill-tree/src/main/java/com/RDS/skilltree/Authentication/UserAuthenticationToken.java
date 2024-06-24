package com.RDS.skilltree.Authentication;

import com.RDS.skilltree.User.JwtUserModel;
import com.RDS.skilltree.User.UserRoleEnum;
import java.util.List;
import javax.security.auth.Subject;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserAuthenticationToken extends AbstractAuthenticationToken {

    private final JwtUserModel user;

    public UserAuthenticationToken(String role, String rdsUserId) {
        super(List.of(new SimpleGrantedAuthority(UserRoleEnum.fromString(role).name())));

        this.user = new JwtUserModel(rdsUserId, UserRoleEnum.fromString(role));
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public JwtUserModel getPrincipal() {
        return user;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
