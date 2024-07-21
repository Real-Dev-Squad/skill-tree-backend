package com.RDS.skilltree.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    void signInWithRds(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void signInWithRdsCallback(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
