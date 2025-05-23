package com.RDS.skilltree.unit.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.RDS.skilltree.exceptions.InvalidParameterException;
import com.RDS.skilltree.utils.UUIDValidationInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UUIDValidationInterceptorTest {

    @Mock private HttpServletRequest request;

    @Mock private HttpServletResponse response;

    @InjectMocks private UUIDValidationInterceptor interceptor;

    @Test
    public void itShouldReturnTrueIfValidUserIDIsGiven() {
        when(request.getParameter("skillID")).thenReturn(null);
        when(request.getParameter("userID")).thenReturn(UUID.randomUUID().toString());

        assertTrue(interceptor.preHandle(request, response, null));
    }

    @Test
    public void itShouldReturnTrueIfValidSkillIDIsGiven() {
        when(request.getParameter("userID")).thenReturn(null);
        when(request.getParameter("skillID")).thenReturn(UUID.randomUUID().toString());

        assertTrue(interceptor.preHandle(request, response, null));
    }

    @Test
    public void itShouldReturnTrueIfValidUserIDAndValidSkillIDIsGiven() {
        when(request.getParameter("userID")).thenReturn(UUID.randomUUID().toString());
        when(request.getParameter("skillID")).thenReturn(UUID.randomUUID().toString());

        assertTrue(interceptor.preHandle(request, response, null));
    }

    @Test
    public void itShouldReturnFalseIfInvalidUserIDIsGiven() {
        when(request.getParameter("userID")).thenReturn("null");
        when(request.getParameter("skillID")).thenReturn(UUID.randomUUID().toString());

        assertThrows(
                InvalidParameterException.class, () -> interceptor.preHandle(request, response, null));
    }

    @Test
    public void itShouldReturnFalseIfInvalidSkillIDIsGiven() {
        when(request.getParameter("userID")).thenReturn(UUID.randomUUID().toString());
        when(request.getParameter("skillID")).thenReturn("null");

        assertThrows(
                InvalidParameterException.class, () -> interceptor.preHandle(request, response, null));
    }

    @Test
    public void itShouldReturnFalseIfInvalidUserIDAndInvalidSkillIDIsGiven() {
        when(request.getParameter("userID")).thenReturn("invalid-user-id");
        when(request.getParameter("skillID")).thenReturn("invalid-skill-id");

        assertThrows(
                InvalidParameterException.class, () -> interceptor.preHandle(request, response, null));
    }
}
