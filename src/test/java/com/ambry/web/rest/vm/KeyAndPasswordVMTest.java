package com.ambry.web.rest.vm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link KeyAndPasswordVM}.
 */
@DisplayName("KeyAndPasswordVM Unit Tests")
class KeyAndPasswordVMTest {

    private KeyAndPasswordVM keyAndPasswordVM;

    @BeforeEach
    void setUp() {
        keyAndPasswordVM = new KeyAndPasswordVM();
    }

    @Test
    @DisplayName("Should create KeyAndPasswordVM with default values")
    void testDefaultConstructor() {
        assertThat(keyAndPasswordVM).isNotNull();
        assertThat(keyAndPasswordVM.getKey()).isNull();
        assertThat(keyAndPasswordVM.getNewPassword()).isNull();
    }

    @Test
    @DisplayName("Should set and get key")
    void testKey() {
        String key = "test-key-123";
        keyAndPasswordVM.setKey(key);
        assertThat(keyAndPasswordVM.getKey()).isEqualTo(key);
    }

    @Test
    @DisplayName("Should set and get newPassword")
    void testNewPassword() {
        String newPassword = "newpassword123";
        keyAndPasswordVM.setNewPassword(newPassword);
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("Should handle null key")
    void testNullKey() {
        keyAndPasswordVM.setKey(null);
        assertThat(keyAndPasswordVM.getKey()).isNull();
    }

    @Test
    @DisplayName("Should handle null newPassword")
    void testNullNewPassword() {
        keyAndPasswordVM.setNewPassword(null);
        assertThat(keyAndPasswordVM.getNewPassword()).isNull();
    }

    @Test
    @DisplayName("Should handle empty key")
    void testEmptyKey() {
        keyAndPasswordVM.setKey("");
        assertThat(keyAndPasswordVM.getKey()).isEmpty();
    }

    @Test
    @DisplayName("Should handle empty newPassword")
    void testEmptyNewPassword() {
        keyAndPasswordVM.setNewPassword("");
        assertThat(keyAndPasswordVM.getNewPassword()).isEmpty();
    }

    @Test
    @DisplayName("Should handle key with special characters")
    void testKeyWithSpecialCharacters() {
        String key = "key-123_abc@test.com";
        keyAndPasswordVM.setKey(key);
        assertThat(keyAndPasswordVM.getKey()).isEqualTo(key);
    }

    @Test
    @DisplayName("Should handle newPassword with special characters")
    void testNewPasswordWithSpecialCharacters() {
        String newPassword = "NewP@ssw0rd!123#";
        keyAndPasswordVM.setNewPassword(newPassword);
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("Should handle all properties together")
    void testAllProperties() {
        String key = "reset-key-456";
        String newPassword = "newsecurepassword";

        keyAndPasswordVM.setKey(key);
        keyAndPasswordVM.setNewPassword(newPassword);

        assertThat(keyAndPasswordVM.getKey()).isEqualTo(key);
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("Should handle key update")
    void testKeyUpdate() {
        keyAndPasswordVM.setKey("old-key");
        assertThat(keyAndPasswordVM.getKey()).isEqualTo("old-key");

        keyAndPasswordVM.setKey("new-key");
        assertThat(keyAndPasswordVM.getKey()).isEqualTo("new-key");
    }

    @Test
    @DisplayName("Should handle newPassword update")
    void testNewPasswordUpdate() {
        keyAndPasswordVM.setNewPassword("oldpassword");
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo("oldpassword");

        keyAndPasswordVM.setNewPassword("newpassword");
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo("newpassword");
    }

    @Test
    @DisplayName("Should handle long key")
    void testLongKey() {
        String longKey = "a".repeat(200);
        keyAndPasswordVM.setKey(longKey);
        assertThat(keyAndPasswordVM.getKey()).isEqualTo(longKey);
        assertThat(keyAndPasswordVM.getKey().length()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should handle long newPassword")
    void testLongNewPassword() {
        String longPassword = "a".repeat(200);
        keyAndPasswordVM.setNewPassword(longPassword);
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo(longPassword);
        assertThat(keyAndPasswordVM.getNewPassword().length()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should handle key with UUID format")
    void testKeyWithUUIDFormat() {
        String uuidKey = "550e8400-e29b-41d4-a716-446655440000";
        keyAndPasswordVM.setKey(uuidKey);
        assertThat(keyAndPasswordVM.getKey()).isEqualTo(uuidKey);
    }

    @Test
    @DisplayName("Should handle newPassword with minimum length")
    void testNewPasswordMinLength() {
        String shortPassword = "abcd";
        keyAndPasswordVM.setNewPassword(shortPassword);
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo(shortPassword);
        assertThat(keyAndPasswordVM.getNewPassword().length()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should handle key with whitespace")
    void testKeyWithWhitespace() {
        String key = "  test-key  ";
        keyAndPasswordVM.setKey(key);
        assertThat(keyAndPasswordVM.getKey()).isEqualTo(key);
    }

    @Test
    @DisplayName("Should handle newPassword with whitespace")
    void testNewPasswordWithWhitespace() {
        String newPassword = "  newpassword  ";
        keyAndPasswordVM.setNewPassword(newPassword);
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("Should handle key with numbers only")
    void testKeyWithNumbersOnly() {
        String key = "1234567890";
        keyAndPasswordVM.setKey(key);
        assertThat(keyAndPasswordVM.getKey()).isEqualTo(key);
    }

    @Test
    @DisplayName("Should handle newPassword with numbers only")
    void testNewPasswordWithNumbersOnly() {
        String newPassword = "12345678";
        keyAndPasswordVM.setNewPassword(newPassword);
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("Should handle key with letters only")
    void testKeyWithLettersOnly() {
        String key = "abcdefghijklmnop";
        keyAndPasswordVM.setKey(key);
        assertThat(keyAndPasswordVM.getKey()).isEqualTo(key);
    }

    @Test
    @DisplayName("Should handle newPassword with letters only")
    void testNewPasswordWithLettersOnly() {
        String newPassword = "abcdefghijklmnop";
        keyAndPasswordVM.setNewPassword(newPassword);
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("Should handle key with mixed case")
    void testKeyWithMixedCase() {
        String key = "Key-123-ABC-def";
        keyAndPasswordVM.setKey(key);
        assertThat(keyAndPasswordVM.getKey()).isEqualTo(key);
    }

    @Test
    @DisplayName("Should handle newPassword with mixed case")
    void testNewPasswordWithMixedCase() {
        String newPassword = "NewPassword123";
        keyAndPasswordVM.setNewPassword(newPassword);
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("Should handle setting key and newPassword independently")
    void testIndependentProperties() {
        // Set only key
        keyAndPasswordVM.setKey("test-key");
        assertThat(keyAndPasswordVM.getKey()).isEqualTo("test-key");
        assertThat(keyAndPasswordVM.getNewPassword()).isNull();

        // Set only newPassword
        keyAndPasswordVM = new KeyAndPasswordVM();
        keyAndPasswordVM.setNewPassword("test-password");
        assertThat(keyAndPasswordVM.getKey()).isNull();
        assertThat(keyAndPasswordVM.getNewPassword()).isEqualTo("test-password");
    }
}

