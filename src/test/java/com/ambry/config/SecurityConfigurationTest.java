package com.ambry.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * Unit tests for {@link SecurityConfiguration}.
 */
class SecurityConfigurationTest {

    private SecurityConfiguration securityConfiguration;
    private UnoFrameworkConfig unoFrameworkConfig;
    private ApplicationProperties appProperties;

    @BeforeEach
    void setUp() {
        unoFrameworkConfig = new UnoFrameworkConfig();
        appProperties = new ApplicationProperties();
        securityConfiguration = new SecurityConfiguration(appProperties,unoFrameworkConfig);
    }

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = securityConfiguration.passwordEncoder();
        assertThat(passwordEncoder).isNotNull();
    }

    @Test
    void testPasswordEncoderEncodesPassword() {
        PasswordEncoder passwordEncoder = securityConfiguration.passwordEncoder();
        String rawPassword = "testPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    void testPasswordEncoderDifferentPasswords() {
        PasswordEncoder passwordEncoder = securityConfiguration.passwordEncoder();
        String password1 = "password1";
        String password2 = "password2";

        String encoded1 = passwordEncoder.encode(password1);
        String encoded2 = passwordEncoder.encode(password2);

        assertThat(encoded1).isNotEqualTo(encoded2);
        assertThat(passwordEncoder.matches(password1, encoded1)).isTrue();
        assertThat(passwordEncoder.matches(password2, encoded2)).isTrue();
    }

    @Test
    void testMvcRequestMatcherBuilderBean() throws Exception {
        HandlerMappingIntrospector introspector = mock(HandlerMappingIntrospector.class);
        var builder = securityConfiguration.mvc(introspector);
        assertThat(builder).isNotNull();
    }
}

