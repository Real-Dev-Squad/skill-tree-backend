package com.RDS.skilltree.Authentication;

import com.RDS.skilltree.User.UserModel;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import javax.security.auth.Subject;

public class UserAuthenticationToken extends AbstractAuthenticationToken {

    private final UserModel user;

    public UserAuthenticationToken(UserModel user) {
        super(null);
        this.user = user;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
