package com.RDS.skilltree.Health;

import com.RDS.skilltree.metrics.MetricService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/health")
@Slf4j
public class HealthCheckController {

    private final MetricService metricService;

    @Autowired
    public HealthCheckController(MetricService metricService) {
        this.metricService = metricService;
    }

    @GetMapping("")
    public Map<String, Object> checkHealth() {
        double uptime = metricService.getUptime();
        return Map.of("uptimeInSeconds", uptime);
    }
}
