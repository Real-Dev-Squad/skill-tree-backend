package com.RDS.skilltree.Authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * A description of the entire Java function.
     *
     * @param  request				the HttpServletRequest object representing the client's request
     * @param  response				the HttpServletResponse object representing the server's response
     * @param  authException		the AuthenticationException object representing the exception that occurred during authentication
     * @throws IOException			if an I/O error occurs while writing the response
     * @throws ServletException	if the request could not be handled
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.println("Access Denied !! " + authException.getMessage());
    }
}
