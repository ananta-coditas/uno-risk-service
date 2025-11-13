package com.ambry.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link LoggingConfiguration}.
 */
class LoggingConfigurationTest {

    private ObjectMapper objectMapper;
    private UnoFrameworkConfig unoFrameworkConfig;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        unoFrameworkConfig = new UnoFrameworkConfig();
    }

    @Test
    void testLoggingConfigurationCreation() throws JsonProcessingException {
        String appName = "test-app";
        String serverPort = "8080";

        LoggingConfiguration config = new LoggingConfiguration(
            appName,
            serverPort,
            unoFrameworkConfig,
            objectMapper
        );

        assertThat(config).isNotNull();
    }

    @Test
    void testLoggingConfigurationWithJsonFormat() throws JsonProcessingException {
        String appName = "test-app";
        String serverPort = "8080";

        // Configure to use JSON format
        unoFrameworkConfig.getLogging().setUseJsonFormat(true);
        unoFrameworkConfig.getLogging().getLogstash().setEnabled(false);

        LoggingConfiguration config = new LoggingConfiguration(
            appName,
            serverPort,
            unoFrameworkConfig,
            objectMapper
        );

        assertThat(config).isNotNull();
    }

    @Test
    void testLoggingConfigurationWithLogstash() throws JsonProcessingException {
        String appName = "test-app";
        String serverPort = "8080";

        // Configure to use Logstash
        unoFrameworkConfig.getLogging().setUseJsonFormat(false);
        unoFrameworkConfig.getLogging().getLogstash().setEnabled(true);

        LoggingConfiguration config = new LoggingConfiguration(
            appName,
            serverPort,
            unoFrameworkConfig,
            objectMapper
        );

        assertThat(config).isNotNull();
    }

    @Test
    void testLoggingConfigurationWithBothJsonAndLogstash() throws JsonProcessingException {
        String appName = "test-app";
        String serverPort = "8080";

        // Configure to use both JSON format and Logstash
        unoFrameworkConfig.getLogging().setUseJsonFormat(true);
        unoFrameworkConfig.getLogging().getLogstash().setEnabled(true);

        LoggingConfiguration config = new LoggingConfiguration(
            appName,
            serverPort,
            unoFrameworkConfig,
            objectMapper
        );

        assertThat(config).isNotNull();
    }
}

