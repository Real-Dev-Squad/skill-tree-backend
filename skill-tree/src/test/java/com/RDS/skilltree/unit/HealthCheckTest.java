package com.RDS.skilltree.unit;

import com.RDS.skilltree.Health.HealthCheckController;
import com.RDS.skilltree.metrics.MetricService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HealthCheckTest {

    @Mock
    private MetricService metricService;

    private HealthCheckController healthCheckController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        healthCheckController = new HealthCheckController(metricService);
    }

    @Test
    void checkHealth() {
        // Setup
        when(metricService.getUptime()).thenReturn(123.0);

        // Execute
        var result = healthCheckController.checkHealth();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("uptimeInSeconds"));
        assertEquals(123.0, result.get("uptimeInSeconds"));
    }
}
