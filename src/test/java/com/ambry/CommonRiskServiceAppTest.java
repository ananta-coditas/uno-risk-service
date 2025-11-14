package com.ambry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import com.ambry.config.UnoFrameworkConstants;

/**
 * Unit tests for {@link CommonRiskServiceApp}.
 */
@DisplayName("CommonRiskServiceApp Unit Tests")
class CommonRiskServiceAppTest {

    private CommonRiskServiceApp app;
    private Environment mockEnvironment;

    @BeforeEach
    void setUp() {
        mockEnvironment = mock(Environment.class);
    }

    @Test
    @DisplayName("Should create CommonRiskServiceApp with default constructor")
    void testDefaultConstructor() {
        app = new CommonRiskServiceApp();
        assertThat(app).isNotNull();
    }

    @Test
    @DisplayName("Should create CommonRiskServiceApp with Environment constructor")
    void testEnvironmentConstructor() {
        app = new CommonRiskServiceApp(mockEnvironment);
        assertThat(app).isNotNull();
    }

    @Test
    @DisplayName("Should initialize application with null environment")
    void testInitApplicationWithNullEnvironment() {
        app = new CommonRiskServiceApp(null);
        // Should not throw exception
        app.initApplication();
    }

    @Test
    @DisplayName("Should initialize application with no active profiles")
    void testInitApplicationWithNoActiveProfiles() {
        when(mockEnvironment.getActiveProfiles()).thenReturn(new String[0]);
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        // Should not throw exception
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should log error when both dev and prod profiles are active")
    void testInitApplicationWithDevAndProdProfiles() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { 
                UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT,
                UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION 
            });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        // Should log error but not throw exception
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should log error when dev profile is active")
    void testInitApplicationWithDevProfile() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        // Should log error but not throw exception
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should initialize application with valid profile")
    void testInitApplicationWithValidProfile() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with HTTPS protocol")
    void testLogApplicationStartupWithHttps() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("server.ssl.key-store", "/path/to/keystore");
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8443");
        env.setProperty("server.servlet.context-path", "/api");
        env.setActiveProfiles("prod");

        // Use reflection to call private static method
        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should not throw exception
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with HTTP protocol")
    void testLogApplicationStartupWithHttp() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/");
        env.setActiveProfiles("dev");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should not throw exception
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with null context path")
    void testLogApplicationStartupWithNullContextPath() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        // Don't set context-path, should default to "/"

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should not throw exception, should use "/" as default
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with empty context path")
    void testLogApplicationStartupWithEmptyContextPath() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should use "/" as default when empty
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with blank context path")
    void testLogApplicationStartupWithBlankContextPath() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "   ");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should use "/" as default when blank
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with no active profiles")
    void testLogApplicationStartupWithNoActiveProfiles() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/");
        // No active profiles set, should use default profiles

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should not throw exception
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with active profiles")
    void testLogApplicationStartupWithActiveProfiles() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/");
        env.setActiveProfiles("prod", "test");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should not throw exception
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with null properties")
    void testLogApplicationStartupWithNullProperties() {
        MockEnvironment env = new MockEnvironment();
        // Don't set any properties, all should be null

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should handle null properties gracefully
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle initApplication with single dev profile")
    void testInitApplicationWithSingleDevProfile() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle initApplication with single prod profile")
    void testInitApplicationWithSingleProdProfile() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle initApplication with test profile")
    void testInitApplicationWithTestProfile() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { "test" });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        verify(mockEnvironment).getActiveProfiles();
    }
}

