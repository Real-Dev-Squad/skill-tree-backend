package com.RDS.skilltree.Authentication;

import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRole;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.Subject;
import java.util.List;
import java.util.UUID;

public class UserAuthenticationToken extends AbstractAuthenticationToken {
private final String rdsUserId;
private final UserRole role ;

    public UserAuthenticationToken(String role, String rdsUserId) {
        super(List.of(new SimpleGrantedAuthority(role)));
//        System.out.println( List.of(new SimpleGrantedAuthority(role)));

        this.rdsUserId = rdsUserId;
        this.role  = UserRole.fromString(role);
        setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal(){
        return UserModel.builder().rdsUserId(rdsUserId).role(role).build();
    }


    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
