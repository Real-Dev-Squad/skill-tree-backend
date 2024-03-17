package com.RDS.skilltree.Authentication;

import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRole;
import java.util.List;
import javax.security.auth.Subject;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserAuthenticationToken extends AbstractAuthenticationToken {

    private final UserModel user;

    public UserAuthenticationToken(String role, String rdsUserId) {
        super(List.of(new SimpleGrantedAuthority(UserRole.fromString(role).name())));
        this.user = UserModel.builder().rdsUserId(rdsUserId).role(UserRole.fromString(role)).build();
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UserModel getPrincipal() {
        return user;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
