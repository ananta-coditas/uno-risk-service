package com.ambry.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.ambry.management.SecurityMetersService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit tests for {@link SecurityJwtConfiguration}.
 */
class SecurityJwtConfigurationTest {

    private SecurityJwtConfiguration securityJwtConfiguration;
    private SecurityMetersService securityMetersService;

    @BeforeEach
    void setUp() {
        securityJwtConfiguration = new SecurityJwtConfiguration();
        securityMetersService = new SecurityMetersService(new SimpleMeterRegistry());

        // Set test values using reflection
        String testJwtKey = "dGVzdC1zZWNyZXQta2V5LWZvci1qdW5pdC10ZXN0aW5nLXB1cnBvc2VzLW9ubHk="; // base64 encoded test key
        String testJwkSetUri = "http://localhost:8080/.well-known/jwks.json";

        ReflectionTestUtils.setField(securityJwtConfiguration, "jwtKey", (Object) testJwtKey);
        ReflectionTestUtils.setField(securityJwtConfiguration, "jwkSetKeyUri", (Object) testJwkSetUri);
    }

    @Test
    void testJwtEncoderBean() {
        JwtEncoder jwtEncoder = securityJwtConfiguration.jwtEncoder();
        assertThat(jwtEncoder).isNotNull();
    }

    @Test
    void testJwtDecoderBean() {
        JwtDecoder jwtDecoder = securityJwtConfiguration.jwtDecoder(securityMetersService);
        assertThat(jwtDecoder).isNotNull();
    }
}

