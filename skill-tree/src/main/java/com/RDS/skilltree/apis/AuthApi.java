package com.RDS.skilltree.apis;

import com.RDS.skilltree.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auth")
public class AuthApi {
    private final AuthService authService;


    @GetMapping("/rds/login")
    public void signInWithRds(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.signInWithRds(request, response);
    }

    @GetMapping("/rds/callback")
    public void signInWithRdsCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.signInWithRdsCallback(request, response);
    }
}
