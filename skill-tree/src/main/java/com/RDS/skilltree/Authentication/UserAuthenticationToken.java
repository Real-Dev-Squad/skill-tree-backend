package com.RDS.skilltree.Authentication;

import com.RDS.skilltree.User.UserModel;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import javax.security.auth.Subject;
import java.util.UUID;

public class UserAuthenticationToken extends AbstractAuthenticationToken {
private final String userId;


    public UserAuthenticationToken(String userId) {
        super(null);
        this.userId = userId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
