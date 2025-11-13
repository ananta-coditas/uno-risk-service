package com.ambry.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.ambry.aop.logging.LoggingAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

/**
 * Unit tests for {@link LoggingAspectConfiguration}.
 */
@SpringBootTest(classes = { LoggingAspectConfiguration.class })
@ActiveProfiles("dev")
class LoggingAspectConfigurationTest {

    @Autowired(required = false)
    private LoggingAspect loggingAspect;

    @Autowired
    private Environment environment;

    @Test
    void testLoggingAspectBeanExistsInDevProfile() {
        // This test will only pass when the dev profile is active
        // The bean is conditionally created based on profile
        if (environment.acceptsProfiles(org.springframework.core.env.Profiles.of("dev"))) {
            assertThat(loggingAspect).isNotNull();
        }
    }

    @Test
    void testLoggingAspectConfigurationClassExists() {
        LoggingAspectConfiguration config = new LoggingAspectConfiguration();
        assertThat(config).isNotNull();
    }
}

