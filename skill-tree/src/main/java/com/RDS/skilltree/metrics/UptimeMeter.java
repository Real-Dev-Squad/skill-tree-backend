package com.RDS.skilltree.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class UptimeMeter {

    private final long startTime;

    public UptimeMeter(MeterRegistry meterRegistry) {
        this.startTime = System.currentTimeMillis();
        meterRegistry.gauge("custom.uptime", this,
                UptimeMeter -> (System.currentTimeMillis() - UptimeMeter.startTime) / 1000.0);
    }
}
