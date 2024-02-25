package com.RDS.skilltree.unit;

import com.RDS.skilltree.Filters.JWTAuthenticationFilter;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRole;
import com.RDS.skilltree.User.UserService;
import com.RDS.skilltree.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class JWTAuthenticationFilterTest  {

    @Mock
    private JWTUtils jwtUtils;

    @InjectMocks
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Value("${cookieName}")
    private String cookieName = "rds-session-v2-development";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks before each test
    }



    @Disabled
    @Test
    void doFilterInternal_ValidToken_ShouldSetAuthentication() throws Exception {
        // Arrange
        MockitoAnnotations.initMocks(this);
        String validToken = "validToken";
        when(jwtUtils.validateToken(validToken)).thenReturn(true);
        when(jwtUtils.getRDSUserId(validToken)).thenReturn("userId");
        when(jwtUtils.getUserRole(validToken)).thenReturn(UserRole.USER.label);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie(cookieName, validToken));
//        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(validToken);

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);

        verify(jwtUtils).validateToken(validToken);
        verify(jwtUtils).getRDSUserId(validToken);
        verify(jwtUtils).getUserRole(validToken);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        verify(authentication instanceof UserAuthenticationToken);

        UserModel user = (UserModel) authentication.getPrincipal();
        assertEquals(UserRole.USER, user.getRole());

//        verify("userId", user.getRdsUserId());
    }

    @Disabled
    @Test
    void doFilterInternal_InvalidToken_ShouldThrowError() throws ServletException, IOException, Exception {
        // Arrange
        MockitoAnnotations.initMocks(this);
        String invalidToken = "invalidToken";
        when(jwtUtils.validateToken(invalidToken)).thenReturn(false);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("yourCookieName", invalidToken));

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // Act and Assert
        assertThrows(RuntimeException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

        // Ensure that the SecurityContextHolder does not contain authentication
        assertNull(SecurityContextHolder.getContext().getAuthentication());

    }
}

