package com.ambry.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Test configuration to provide a MeterRegistry bean for integration tests.
 * This is needed because SecurityMetersService requires a MeterRegistry.
 */
@TestConfiguration
public class TestMeterRegistryConfiguration {

    @Bean
    @Primary
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }
}

