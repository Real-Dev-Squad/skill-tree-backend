package com.RDS.skilltree.services;

import com.RDS.skilltree.services.external.RdsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final RdsService rdsService;

    @Value("${skilltree.frontendBaseUrl}")
    private String skilltreeFrontendBaseUrl;

    @Value("${skilltree.backendBaseUrl}")
    private String skilltreeBackendBaseUrl;

    @Override
    public void signInWithRds(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String callbackUrl = skilltreeBackendBaseUrl + "/v1/auth/rds/callback";
        String redirectUrl = rdsService.signIn(callbackUrl);

        response.sendRedirect(redirectUrl);
    }

    @Override
    public void signInWithRdsCallback(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String dashboardUrl = skilltreeFrontendBaseUrl + "/dashboard";
        String authCookie = request.getHeader(HttpHeaders.COOKIE);

        response.addHeader(HttpHeaders.SET_COOKIE, authCookie);
        response.sendRedirect(dashboardUrl);
    }
}
