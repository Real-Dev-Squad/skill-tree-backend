package com.RDS.skilltree.Authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public AuthEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

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
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        this.resolver.resolveException(request, response, null, authException);
    }
}
