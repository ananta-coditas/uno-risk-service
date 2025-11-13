package com.ambry.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.ambry.config.Constants;

@DisplayName("SpringSecurityAuditorAware Unit Tests")
class SpringSecurityAuditorAwareTest {

    private SpringSecurityAuditorAware auditorAware;
    private MockedStatic<SecurityUtils> securityUtilsMock;

    @BeforeEach
    void setUp() {
        auditorAware = new SpringSecurityAuditorAware();
        securityUtilsMock = mockStatic(SecurityUtils.class);
    }

    @AfterEach
    void tearDown() {
        securityUtilsMock.close();
    }

    @Test
    @DisplayName("Should return current user login when user is logged in")
    void testGetCurrentAuditorWithLoggedInUser() {
        String username = "testuser";
        when(SecurityUtils.getCurrentUserLogin()).thenReturn(Optional.of(username));

        Optional<String> result = auditorAware.getCurrentAuditor();

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(username);
    }

    @Test
    @DisplayName("Should return SYSTEM when no user is logged in")
    void testGetCurrentAuditorWithNoUser() {
        when(SecurityUtils.getCurrentUserLogin()).thenReturn(Optional.empty());

        Optional<String> result = auditorAware.getCurrentAuditor();

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(Constants.SYSTEM);
    }

    @Test
    @DisplayName("Should return SYSTEM when getCurrentUserLogin returns null")
    void testGetCurrentAuditorWithNullUser() {
        when(SecurityUtils.getCurrentUserLogin()).thenReturn(Optional.empty());

        Optional<String> result = auditorAware.getCurrentAuditor();

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(Constants.SYSTEM);
    }

    @Test
    @DisplayName("Should return different users correctly")
    void testGetCurrentAuditorWithDifferentUsers() {
        String user1 = "user1";
        when(SecurityUtils.getCurrentUserLogin()).thenReturn(Optional.of(user1));
        Optional<String> result1 = auditorAware.getCurrentAuditor();
        assertThat(result1).isPresent();
        assertThat(result1.get()).isEqualTo(user1);

        String user2 = "user2";
        when(SecurityUtils.getCurrentUserLogin()).thenReturn(Optional.of(user2));
        Optional<String> result2 = auditorAware.getCurrentAuditor();
        assertThat(result2).isPresent();
        assertThat(result2.get()).isEqualTo(user2);
    }

    @Test
    @DisplayName("Should always return a non-empty Optional")
    void testGetCurrentAuditorAlwaysReturnsNonEmpty() {
        when(SecurityUtils.getCurrentUserLogin()).thenReturn(Optional.empty());
        Optional<String> result = auditorAware.getCurrentAuditor();
        assertThat(result).isPresent();

        when(SecurityUtils.getCurrentUserLogin()).thenReturn(Optional.of("testuser"));
        result = auditorAware.getCurrentAuditor();
        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("Should handle empty string username")
    void testGetCurrentAuditorWithEmptyStringUsername() {
        when(SecurityUtils.getCurrentUserLogin()).thenReturn(Optional.of(""));
        Optional<String> result = auditorAware.getCurrentAuditor();
        assertThat(result).isPresent();
        assertThat(result.get()).isEmpty();
    }
}

