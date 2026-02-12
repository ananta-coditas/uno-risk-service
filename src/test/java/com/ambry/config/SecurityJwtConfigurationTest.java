package com.ambry.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.ambry.management.SecurityMetersService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit tests for {@link SecurityJwtConfiguration}.
 */
class SecurityJwtConfigurationTest {

    private SecurityJwtConfiguration securityJwtConfiguration;
    private SecurityMetersService securityMetersService;
    private SecretsManager secretsManager;

    @BeforeEach
    void setUp() {
        secretsManager = mock(SecretsManager.class);
        securityJwtConfiguration = new SecurityJwtConfiguration(secretsManager);
        securityMetersService = new SecurityMetersService(new SimpleMeterRegistry());

        // Set test values using reflection
        // Note: jwtKey field is commented out in SecurityJwtConfiguration, so we only set jwkSetKeyUri
        String testJwkSetUri = "http://localhost:8080/.well-known/jwks.json";

        ReflectionTestUtils.setField(securityJwtConfiguration, "jwkSetKeyUri", (Object) testJwkSetUri);
    }

    // @Test
    // void testJwtEncoderBean() {
    //     JwtEncoder jwtEncoder = securityJwtConfiguration.jwtEncoder();
    //     assertThat(jwtEncoder).isNotNull();
    // }

    @Test
    void testJwtDecoderBean() {
        JwtDecoder jwtDecoder = securityJwtConfiguration.jwtDecoder(securityMetersService);
        assertThat(jwtDecoder).isNotNull();
    }
}

