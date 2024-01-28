package com.RDS.skilltree.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Service;

@Service
public class MetricService {

    private final MetricsEndpoint metricsEndpoint;

    @Autowired
    public MetricService(MetricsEndpoint metricsEndpoint) {
        this.metricsEndpoint = metricsEndpoint;
    }

    public double getUptime() {
        return metricsEndpoint.metric("process.uptime", null).getMeasurements()
                .stream()
                .findFirst()
                .map(MetricsEndpoint.Sample::getValue)
                .orElse(0.0);
    }
}
