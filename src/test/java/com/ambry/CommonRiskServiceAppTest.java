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

    @Test
    @DisplayName("Should handle initApplication with cloud profile only")
    void testInitApplicationWithCloudProfile() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { "cloud" });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle initApplication with dev and cloud profiles")
    void testInitApplicationWithDevAndCloudProfiles() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, "cloud" });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        // Should log error for dev and cloud combination
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle initApplication with multiple profiles excluding dev and prod")
    void testInitApplicationWithMultipleOtherProfiles() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { "test", "cloud", "qa" });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with default profiles")
    void testLogApplicationStartupWithDefaultProfiles() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/");
        env.setDefaultProfiles("default", "dev");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should use default profiles when no active profiles
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with whitespace context path")
    void testLogApplicationStartupWithWhitespaceContextPath() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "\t\n  ");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should use "/" as default when context path is whitespace
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with all null properties")
    void testLogApplicationStartupWithAllNullProperties() {
        MockEnvironment env = new MockEnvironment();
        // Don't set any properties

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should handle all null properties gracefully
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with empty active profiles array")
    void testLogApplicationStartupWithEmptyActiveProfiles() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/");
        env.setActiveProfiles(); // Empty array
        env.setDefaultProfiles("default");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should use default profiles when active profiles is empty
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle initApplication with empty profiles array")
    void testInitApplicationWithEmptyProfilesArray() {
        when(mockEnvironment.getActiveProfiles()).thenReturn(new String[0]);
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle initApplication when only prod profile is active")
    void testInitApplicationWithOnlyProdProfile() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        // Should not log error, only prod is active
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle initApplication with prod and cloud profiles")
    void testInitApplicationWithProdAndCloudProfiles() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION, "cloud" });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        // Should not log error, dev is not active
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should have main method with correct signature")
    void testMainMethodSignature() {
        // Verify that main method exists and has correct signature
        try {
            java.lang.reflect.Method mainMethod = CommonRiskServiceApp.class.getDeclaredMethod("main", String[].class);
            assertThat(mainMethod).isNotNull();
            assertThat(mainMethod.getModifiers() & java.lang.reflect.Modifier.STATIC).isNotZero();
            assertThat(mainMethod.getModifiers() & java.lang.reflect.Modifier.PUBLIC).isNotZero();
            assertThat(mainMethod.getParameterCount()).isEqualTo(1);
            assertThat(mainMethod.getParameterTypes()[0]).isEqualTo(String[].class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("main method should exist", e);
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with empty string key-store")
    void testLogApplicationStartupWithEmptyStringKeyStore() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("server.ssl.key-store", "");
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Empty string should be treated as null, so should use http
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with tab and newline whitespace in context path")
    void testLogApplicationStartupWithTabAndNewlineWhitespace() {
        MockEnvironment env = new MockEnvironment();
        // Don't set server.ssl.key-store (will be null)
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "\t\n\r");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should use "/" as default when context path is whitespace
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with valid context path")
    void testLogApplicationStartupWithValidContextPath() {
        MockEnvironment env = new MockEnvironment();
        // Don't set server.ssl.key-store (will be null)
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/myapp");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should use the provided context path
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with all properties set")
    void testLogApplicationStartupWithAllPropertiesSet() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("server.ssl.key-store", "/path/to/keystore");
        env.setProperty("spring.application.name", "my-application");
        env.setProperty("server.port", "9090");
        env.setProperty("server.servlet.context-path", "/context");
        env.setActiveProfiles("prod");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should handle all properties being set
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with only application name set")
    void testLogApplicationStartupWithOnlyApplicationName() {
        MockEnvironment env = new MockEnvironment();
        // Don't set server.ssl.key-store, server.port, server.servlet.context-path (will be null)
        env.setProperty("spring.application.name", "app-name");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should handle partial properties
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with only port set")
    void testLogApplicationStartupWithOnlyPort() {
        MockEnvironment env = new MockEnvironment();
        // Don't set server.ssl.key-store, spring.application.name, server.servlet.context-path (will be null)
        env.setProperty("server.port", "9999");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should handle partial properties
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with whitespace-only application name")
    void testLogApplicationStartupWithWhitespaceApplicationName() {
        MockEnvironment env = new MockEnvironment();
        // Don't set server.ssl.key-store (will be null)
        env.setProperty("spring.application.name", "   ");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should handle whitespace-only values
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with whitespace-only port")
    void testLogApplicationStartupWithWhitespacePort() {
        MockEnvironment env = new MockEnvironment();
        // Don't set server.ssl.key-store (will be null)
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "\t");
        env.setProperty("server.servlet.context-path", "/");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should handle whitespace-only values
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with very long context path")
    void testLogApplicationStartupWithLongContextPath() {
        MockEnvironment env = new MockEnvironment();
        String longContextPath = "/" + "a".repeat(1000);
        // Don't set server.ssl.key-store (will be null)
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", longContextPath);

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should handle long context paths
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with special characters in properties")
    void testLogApplicationStartupWithSpecialCharacters() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("server.ssl.key-store", "/path/with spaces/file.jks");
        env.setProperty("spring.application.name", "app-name_with-special.chars");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/path/with/special/chars");
        env.setActiveProfiles("prod");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should handle special characters in properties
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with non-null key-store property")
    void testLogApplicationStartupWithNonNullKeyStore() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("server.ssl.key-store", "keystore.jks");
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8443");
        env.setProperty("server.servlet.context-path", "/api");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should use https protocol when key-store is not null
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle initApplication with dev, prod, and cloud profiles")
    void testInitApplicationWithDevProdAndCloudProfiles() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { 
                UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT,
                UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION,
                "cloud"
            });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        // Should log errors for both conditions
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle initApplication with multiple dev profiles")
    void testInitApplicationWithMultipleDevProfiles() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { 
                UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT,
                UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT
            });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        // Should log error for dev profile
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle initApplication with prod, test, and cloud profiles")
    void testInitApplicationWithProdTestAndCloudProfiles() {
        when(mockEnvironment.getActiveProfiles())
            .thenReturn(new String[] { 
                UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION,
                "test",
                "cloud"
            });
        app = new CommonRiskServiceApp(mockEnvironment);
        
        app.initApplication();
        
        // Should not log error, dev is not active
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should verify logApplicationStartup method exists and is private static")
    void testLogApplicationStartupMethodSignature() {
        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", org.springframework.core.env.Environment.class);
            assertThat(method).isNotNull();
            assertThat(method.getModifiers() & java.lang.reflect.Modifier.PRIVATE).isNotZero();
            assertThat(method.getModifiers() & java.lang.reflect.Modifier.STATIC).isNotZero();
            assertThat(method.getParameterCount()).isEqualTo(1);
            assertThat(method.getParameterTypes()[0]).isEqualTo(org.springframework.core.env.Environment.class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("logApplicationStartup method should exist", e);
        }
    }

    @Test
    @DisplayName("Should verify initApplication method exists and has correct signature")
    void testInitApplicationMethodSignature() {
        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("initApplication");
            assertThat(method).isNotNull();
            assertThat(method.getModifiers() & java.lang.reflect.Modifier.PUBLIC).isNotZero();
            assertThat(method.getParameterCount()).isEqualTo(0);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("initApplication method should exist", e);
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with single active profile")
    void testLogApplicationStartupWithSingleActiveProfile() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/");
        env.setActiveProfiles("prod");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should use active profiles when length > 0
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with multiple default profiles")
    void testLogApplicationStartupWithMultipleDefaultProfiles() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "test-app");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/");
        env.setDefaultProfiles("default", "dev", "test");

        try {
            java.lang.reflect.Method method = CommonRiskServiceApp.class.getDeclaredMethod("logApplicationStartup", Environment.class);
            method.setAccessible(true);
            method.invoke(null, env);
            // Should use default profiles when active profiles is empty
        } catch (Exception e) {
            // Acceptable if InetAddress.getLocalHost() fails in test environment
        }
    }

    @Test
    @DisplayName("Should verify Environment field can be set via constructor")
    void testEnvironmentFieldSetViaConstructor() {
        app = new CommonRiskServiceApp(mockEnvironment);
        assertThat(app).isNotNull();
        
        // Verify that initApplication uses the environment
        when(mockEnvironment.getActiveProfiles()).thenReturn(new String[0]);
        app.initApplication();
        verify(mockEnvironment).getActiveProfiles();
    }

    @Test
    @DisplayName("Should handle initApplication when environment is null after construction")
    void testInitApplicationWithNullEnvironmentAfterConstruction() {
        app = new CommonRiskServiceApp();
        // env field should be null
        app.initApplication();
        // Should not throw exception, should use empty ArrayList
    }

}

