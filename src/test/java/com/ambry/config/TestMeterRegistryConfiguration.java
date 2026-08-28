package com.ambry.config;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.tracing.BaggageInScope;
import io.micrometer.tracing.Tracer;
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

    @Bean
    @Primary
    public Tracer tracer() {
        Tracer tracer = mock(Tracer.class);
        BaggageInScope scope = mock(BaggageInScope.class);
        when(tracer.createBaggageInScope(anyString(), anyString())).thenReturn(scope);
        return tracer;
    }
}
