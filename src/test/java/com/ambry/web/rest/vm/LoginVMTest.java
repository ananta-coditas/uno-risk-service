package com.ambry.web.rest.vm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link LoginVM}.
 */
@DisplayName("LoginVM Unit Tests")
class LoginVMTest {

    private LoginVM loginVM;

    @BeforeEach
    void setUp() {
        loginVM = new LoginVM();
    }

    @Test
    @DisplayName("Should create LoginVM with default values")
    void testDefaultConstructor() {
        assertThat(loginVM).isNotNull();
        assertThat(loginVM.getUsername()).isNull();
        assertThat(loginVM.getPassword()).isNull();
        assertThat(loginVM.isRememberMe()).isFalse();
    }

    @Test
    @DisplayName("Should set and get username")
    void testUsername() {
        String username = "testuser";
        loginVM.setUsername(username);
        assertThat(loginVM.getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("Should set and get password")
    void testPassword() {
        String password = "testpassword123";
        loginVM.setPassword(password);
        assertThat(loginVM.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("Should set and get rememberMe flag")
    void testRememberMe() {
        loginVM.setRememberMe(true);
        assertThat(loginVM.isRememberMe()).isTrue();

        loginVM.setRememberMe(false);
        assertThat(loginVM.isRememberMe()).isFalse();
    }

    @Test
    @DisplayName("Should handle null username")
    void testNullUsername() {
        loginVM.setUsername(null);
        assertThat(loginVM.getUsername()).isNull();
    }

    @Test
    @DisplayName("Should handle null password")
    void testNullPassword() {
        loginVM.setPassword(null);
        assertThat(loginVM.getPassword()).isNull();
    }

    @Test
    @DisplayName("Should handle empty username")
    void testEmptyUsername() {
        loginVM.setUsername("");
        assertThat(loginVM.getUsername()).isEmpty();
    }

    @Test
    @DisplayName("Should handle empty password")
    void testEmptyPassword() {
        loginVM.setPassword("");
        assertThat(loginVM.getPassword()).isEmpty();
    }

    @Test
    @DisplayName("Should handle username with maximum length")
    void testUsernameMaxLength() {
        String longUsername = "a".repeat(50);
        loginVM.setUsername(longUsername);
        assertThat(loginVM.getUsername()).isEqualTo(longUsername);
        assertThat(loginVM.getUsername().length()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should handle password with maximum length")
    void testPasswordMaxLength() {
        String longPassword = "a".repeat(100);
        loginVM.setPassword(longPassword);
        assertThat(loginVM.getPassword()).isEqualTo(longPassword);
        assertThat(loginVM.getPassword().length()).isEqualTo(100);
    }

    @Test
    @DisplayName("Should handle username with minimum length")
    void testUsernameMinLength() {
        String shortUsername = "a";
        loginVM.setUsername(shortUsername);
        assertThat(loginVM.getUsername()).isEqualTo(shortUsername);
        assertThat(loginVM.getUsername().length()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should handle password with minimum length")
    void testPasswordMinLength() {
        String shortPassword = "abcd";
        loginVM.setPassword(shortPassword);
        assertThat(loginVM.getPassword()).isEqualTo(shortPassword);
        assertThat(loginVM.getPassword().length()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should handle username with special characters")
    void testUsernameWithSpecialCharacters() {
        String username = "user.name_123@test";
        loginVM.setUsername(username);
        assertThat(loginVM.getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("Should handle password with special characters")
    void testPasswordWithSpecialCharacters() {
        String password = "P@ssw0rd!123#";
        loginVM.setPassword(password);
        assertThat(loginVM.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("Should handle all properties together")
    void testAllProperties() {
        String username = "testuser";
        String password = "testpassword";
        boolean rememberMe = true;

        loginVM.setUsername(username);
        loginVM.setPassword(password);
        loginVM.setRememberMe(rememberMe);

        assertThat(loginVM.getUsername()).isEqualTo(username);
        assertThat(loginVM.getPassword()).isEqualTo(password);
        assertThat(loginVM.isRememberMe()).isEqualTo(rememberMe);
    }

    @Test
    @DisplayName("Should return correct toString representation")
    void testToString() {
        String username = "testuser";
        boolean rememberMe = true;

        loginVM.setUsername(username);
        loginVM.setPassword("password");
        loginVM.setRememberMe(rememberMe);

        String result = loginVM.toString();

        assertThat(result).isNotNull();
        assertThat(result).contains("LoginVM");
        assertThat(result).contains("username='" + username + "'");
        assertThat(result).contains("rememberMe=" + rememberMe);
        // Password should not be in toString for security reasons
        assertThat(result).doesNotContain("password");
    }

    @Test
    @DisplayName("Should return correct toString with null username")
    void testToStringWithNullUsername() {
        loginVM.setUsername(null);
        loginVM.setRememberMe(false);

        String result = loginVM.toString();

        assertThat(result).isNotNull();
        assertThat(result).contains("username='null'");
        assertThat(result).contains("rememberMe=false");
    }

    @Test
    @DisplayName("Should return correct toString with rememberMe false")
    void testToStringWithRememberMeFalse() {
        loginVM.setUsername("user");
        loginVM.setRememberMe(false);

        String result = loginVM.toString();

        assertThat(result).isNotNull();
        assertThat(result).contains("rememberMe=false");
    }

    @Test
    @DisplayName("Should handle username update")
    void testUsernameUpdate() {
        loginVM.setUsername("olduser");
        assertThat(loginVM.getUsername()).isEqualTo("olduser");

        loginVM.setUsername("newuser");
        assertThat(loginVM.getUsername()).isEqualTo("newuser");
    }

    @Test
    @DisplayName("Should handle password update")
    void testPasswordUpdate() {
        loginVM.setPassword("oldpassword");
        assertThat(loginVM.getPassword()).isEqualTo("oldpassword");

        loginVM.setPassword("newpassword");
        assertThat(loginVM.getPassword()).isEqualTo("newpassword");
    }

    @Test
    @DisplayName("Should handle rememberMe toggle")
    void testRememberMeToggle() {
        loginVM.setRememberMe(false);
        assertThat(loginVM.isRememberMe()).isFalse();

        loginVM.setRememberMe(true);
        assertThat(loginVM.isRememberMe()).isTrue();

        loginVM.setRememberMe(false);
        assertThat(loginVM.isRememberMe()).isFalse();
    }

    @Test
    @DisplayName("Should handle username with whitespace")
    void testUsernameWithWhitespace() {
        String username = "  testuser  ";
        loginVM.setUsername(username);
        assertThat(loginVM.getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("Should handle password with whitespace")
    void testPasswordWithWhitespace() {
        String password = "  testpassword  ";
        loginVM.setPassword(password);
        assertThat(loginVM.getPassword()).isEqualTo(password);
    }
}

