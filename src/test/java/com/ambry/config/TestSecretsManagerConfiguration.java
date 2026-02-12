package com.ambry.config;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

/**
 * Test configuration to provide SecretsManager-related beans for integration tests.
 */
@TestConfiguration
public class TestSecretsManagerConfiguration {

    @Bean
    @Primary
    public SecretsManagerProperties secretsManagerProperties() {
        SecretsManagerProperties properties = new SecretsManagerProperties();
        properties.setEnabled(false); // Disable for tests
        return properties;
    }

    @Bean
    @Primary
    public SecretsManagerClient secretsManagerClient() {
        // Return a mock client for tests to avoid AWS credential requirements
        return mock(SecretsManagerClient.class);
    }
}

