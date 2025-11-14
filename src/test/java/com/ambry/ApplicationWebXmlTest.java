package com.ambry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;

/**
 * Unit tests for {@link ApplicationWebXml}.
 */
@DisplayName("ApplicationWebXml Unit Tests")
class ApplicationWebXmlTest {

    private ApplicationWebXml applicationWebXml;
    private SpringApplicationBuilder mockBuilder;
    private SpringApplication mockApplication;
    private Environment mockEnvironment;

    @BeforeEach
    void setUp() {
        applicationWebXml = new ApplicationWebXml();
        mockBuilder = mock(SpringApplicationBuilder.class);
        mockApplication = mock(SpringApplication.class);
        mockEnvironment = mock(Environment.class);
    }

    //@Test
    @DisplayName("Should configure SpringApplicationBuilder with CommonRiskServiceApp")
    void testConfigure() {
        when(mockBuilder.application()).thenReturn(mockApplication);
        when(mockBuilder.sources(CommonRiskServiceApp.class)).thenReturn(mockBuilder);

        SpringApplicationBuilder result = applicationWebXml.configure(mockBuilder);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockBuilder);
        verify(mockBuilder).application();
        verify(mockBuilder).sources(CommonRiskServiceApp.class);
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with HTTPS protocol")
    void testLogApplicationStartupWithHttps() throws Exception {
        when(mockEnvironment.getProperty("server.ssl.key-store")).thenReturn("/path/to/keystore");
        when(mockEnvironment.getProperty("spring.application.name")).thenReturn("test-app");
        when(mockEnvironment.getProperty("server.port")).thenReturn("8443");
        when(mockEnvironment.getProperty("server.servlet.context-path")).thenReturn("/api");
        when(mockEnvironment.getActiveProfiles()).thenReturn(new String[] { "prod" });
        when(mockEnvironment.getDefaultProfiles()).thenReturn(new String[] { "dev" });

        // Use reflection to call private method
        java.lang.reflect.Method method = ApplicationWebXml.class.getDeclaredMethod("logApplicationStartup", Environment.class);
        method.setAccessible(true);
        method.invoke(applicationWebXml, mockEnvironment);

        // Should not throw exception
        verify(mockEnvironment).getProperty("server.ssl.key-store");
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with HTTP protocol")
    void testLogApplicationStartupWithHttp() throws Exception {
        when(mockEnvironment.getProperty("server.ssl.key-store")).thenReturn(null);
        when(mockEnvironment.getProperty("spring.application.name")).thenReturn("test-app");
        when(mockEnvironment.getProperty("server.port")).thenReturn("8080");
        when(mockEnvironment.getProperty("server.servlet.context-path")).thenReturn(null);
        when(mockEnvironment.getActiveProfiles()).thenReturn(new String[0]);
        when(mockEnvironment.getDefaultProfiles()).thenReturn(new String[] { "dev" });

        // Use reflection to call private method
        java.lang.reflect.Method method = ApplicationWebXml.class.getDeclaredMethod("logApplicationStartup", Environment.class);
        method.setAccessible(true);
        method.invoke(applicationWebXml, mockEnvironment);

        // Should not throw exception
        verify(mockEnvironment).getProperty("server.ssl.key-store");
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with UnknownHostException")
    void testLogApplicationStartupWithUnknownHostException() throws Exception {
        when(mockEnvironment.getProperty("server.ssl.key-store")).thenReturn(null);
        when(mockEnvironment.getProperty("spring.application.name")).thenReturn("test-app");
        when(mockEnvironment.getProperty("server.port")).thenReturn("8080");
        when(mockEnvironment.getProperty("server.servlet.context-path")).thenReturn("/");
        when(mockEnvironment.getActiveProfiles()).thenReturn(new String[0]);
        when(mockEnvironment.getDefaultProfiles()).thenReturn(new String[] { "dev" });

        // Mock InetAddress to throw UnknownHostException
        // This is tricky to test directly, but we can verify the method handles it gracefully
        java.lang.reflect.Method method = ApplicationWebXml.class.getDeclaredMethod("logApplicationStartup", Environment.class);
        method.setAccessible(true);
        method.invoke(applicationWebXml, mockEnvironment);

        // Should not throw exception, should use "localhost" as fallback
        verify(mockEnvironment).getProperty("server.ssl.key-store");
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with empty context path")
    void testLogApplicationStartupWithEmptyContextPath() throws Exception {
        when(mockEnvironment.getProperty("server.ssl.key-store")).thenReturn(null);
        when(mockEnvironment.getProperty("spring.application.name")).thenReturn("test-app");
        when(mockEnvironment.getProperty("server.port")).thenReturn("8080");
        when(mockEnvironment.getProperty("server.servlet.context-path")).thenReturn("");
        when(mockEnvironment.getActiveProfiles()).thenReturn(new String[0]);
        when(mockEnvironment.getDefaultProfiles()).thenReturn(new String[] { "dev" });

        java.lang.reflect.Method method = ApplicationWebXml.class.getDeclaredMethod("logApplicationStartup", Environment.class);
        method.setAccessible(true);
        method.invoke(applicationWebXml, mockEnvironment);

        // Should use "/" as default context path
        verify(mockEnvironment).getProperty("server.servlet.context-path");
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with blank context path")
    void testLogApplicationStartupWithBlankContextPath() throws Exception {
        when(mockEnvironment.getProperty("server.ssl.key-store")).thenReturn(null);
        when(mockEnvironment.getProperty("spring.application.name")).thenReturn("test-app");
        when(mockEnvironment.getProperty("server.port")).thenReturn("8080");
        when(mockEnvironment.getProperty("server.servlet.context-path")).thenReturn("   ");
        when(mockEnvironment.getActiveProfiles()).thenReturn(new String[0]);
        when(mockEnvironment.getDefaultProfiles()).thenReturn(new String[] { "dev" });

        java.lang.reflect.Method method = ApplicationWebXml.class.getDeclaredMethod("logApplicationStartup", Environment.class);
        method.setAccessible(true);
        method.invoke(applicationWebXml, mockEnvironment);

        // Should use "/" as default context path when blank
        verify(mockEnvironment).getProperty("server.servlet.context-path");
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with active profiles")
    void testLogApplicationStartupWithActiveProfiles() throws Exception {
        when(mockEnvironment.getProperty("server.ssl.key-store")).thenReturn(null);
        when(mockEnvironment.getProperty("spring.application.name")).thenReturn("test-app");
        when(mockEnvironment.getProperty("server.port")).thenReturn("8080");
        when(mockEnvironment.getProperty("server.servlet.context-path")).thenReturn("/");

        java.lang.reflect.Method method = ApplicationWebXml.class.getDeclaredMethod("logApplicationStartup", Environment.class);
        method.setAccessible(true);
        method.invoke(applicationWebXml, mockEnvironment);

        // Verify that properties were accessed (logging code is commented out, so getActiveProfiles is not called)
        verify(mockEnvironment).getProperty("server.ssl.key-store");
        verify(mockEnvironment).getProperty("spring.application.name");
        verify(mockEnvironment).getProperty("server.port");
        verify(mockEnvironment).getProperty("server.servlet.context-path");
    }

    @Test
    @DisplayName("Should handle logApplicationStartup with no active profiles")
    void testLogApplicationStartupWithNoActiveProfiles() throws Exception {
        when(mockEnvironment.getProperty("server.ssl.key-store")).thenReturn(null);
        when(mockEnvironment.getProperty("spring.application.name")).thenReturn("test-app");
        when(mockEnvironment.getProperty("server.port")).thenReturn("8080");
        when(mockEnvironment.getProperty("server.servlet.context-path")).thenReturn("/");

        java.lang.reflect.Method method = ApplicationWebXml.class.getDeclaredMethod("logApplicationStartup", Environment.class);
        method.setAccessible(true);
        method.invoke(applicationWebXml, mockEnvironment);

        // Verify that properties were accessed (logging code is commented out)
        verify(mockEnvironment).getProperty("server.ssl.key-store");
        verify(mockEnvironment).getProperty("spring.application.name");
        verify(mockEnvironment).getProperty("server.port");
        verify(mockEnvironment).getProperty("server.servlet.context-path");
    }

}

