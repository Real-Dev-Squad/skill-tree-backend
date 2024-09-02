package com.RDS.skilltree.apis;

import com.RDS.skilltree.services.MetricService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/health")
public class HealthApi {
    private final MetricService metricService;

    @GetMapping
    public ResponseEntity<Map<String, Double>> getUptime() {
        return new ResponseEntity<>(
                Map.of("uptime in seconds", metricService.getUptime()), HttpStatus.OK);
    }
}
