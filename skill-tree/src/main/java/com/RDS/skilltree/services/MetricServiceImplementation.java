package com.RDS.skilltree.services;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricServiceImplementation implements MetricService {
    private final MetricsEndpoint metricsEndpoint;

    @Override
    public Double getUptime() {
        return metricsEndpoint.metric("process.uptime", null).getMeasurements().stream()
                .findFirst()
                .map(MetricsEndpoint.Sample::getValue)
                .orElse(0.0);
    }
}
