package com.ambry.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

/**
 * Unit tests for {@link SecretsManagerConfig}.
 */
class SecretsManagerConfigTest {

    private SecretsManagerConfig secretsManagerConfig;

    @BeforeEach
    void setUp() {
        secretsManagerConfig = new SecretsManagerConfig();
    }

    @Test
    void testSecretsManagerClientBeanWithDefaultRegion() {
        // Test with default region (us-west-2)
        // Set the region field since @Value annotation doesn't work in unit tests without Spring context
        ReflectionTestUtils.setField(secretsManagerConfig, "awsRegion", "us-west-2");
        SecretsManagerClient client = secretsManagerConfig.secretsManagerClient();
        assertThat(client).isNotNull();
    }

    @Test
    void testSecretsManagerClientBeanWithCustomRegion() {
        // Set a custom region using reflection
        String customRegion = "us-east-1";
        ReflectionTestUtils.setField(secretsManagerConfig, "awsRegion", customRegion);
        
        SecretsManagerClient client = secretsManagerConfig.secretsManagerClient();
        assertThat(client).isNotNull();
    }

    @Test
    void testSecretsManagerClientBeanWithDifferentRegion() {
        // Test with a different region
        String testRegion = "eu-west-1";
        ReflectionTestUtils.setField(secretsManagerConfig, "awsRegion", testRegion);
        
        SecretsManagerClient client = secretsManagerConfig.secretsManagerClient();
        assertThat(client).isNotNull();
    }
}

