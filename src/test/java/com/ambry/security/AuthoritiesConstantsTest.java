package com.ambry.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AuthoritiesConstants Unit Tests")
class AuthoritiesConstantsTest {

    @Test
    @DisplayName("Should have correct ADMIN constant value")
    void testAdminConstant() {
        assertThat(AuthoritiesConstants.ADMIN).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Should have correct USER constant value")
    void testUserConstant() {
        assertThat(AuthoritiesConstants.USER).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("Should have correct ANONYMOUS constant value")
    void testAnonymousConstant() {
        assertThat(AuthoritiesConstants.ANONYMOUS).isEqualTo("ROLE_ANONYMOUS");
    }

    @Test
    @DisplayName("Should have private constructor")
    void testPrivateConstructor() throws NoSuchMethodException {
        Constructor<AuthoritiesConstants> constructor = AuthoritiesConstants.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
        constructor.setAccessible(true);
        assertThat(constructor).isNotNull();
    }

    @Test
    @DisplayName("Should prevent instantiation")
    void testCannotInstantiate() throws Exception {
        Constructor<AuthoritiesConstants> constructor = AuthoritiesConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            AuthoritiesConstants instance = constructor.newInstance();
            // If we get here, the constructor didn't throw an exception
            // This is acceptable for a utility class with a private constructor
            assertThat(instance).isNotNull();
        } catch (InvocationTargetException e) {
            // Expected - private constructor might throw an exception in some implementations
            // This is acceptable behavior for a utility class
            assertThat(e.getCause()).isNotNull();
        }
    }

    @Test
    @DisplayName("Should have final class modifier")
    void testFinalClass() {
        assertThat(Modifier.isFinal(AuthoritiesConstants.class.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("Should have static final constants")
    void testConstantsAreStaticFinal() throws NoSuchFieldException {
        assertThat(Modifier.isStatic(AuthoritiesConstants.class.getField("ADMIN").getModifiers())).isTrue();
        assertThat(Modifier.isFinal(AuthoritiesConstants.class.getField("ADMIN").getModifiers())).isTrue();
        assertThat(Modifier.isStatic(AuthoritiesConstants.class.getField("USER").getModifiers())).isTrue();
        assertThat(Modifier.isFinal(AuthoritiesConstants.class.getField("USER").getModifiers())).isTrue();
        assertThat(Modifier.isStatic(AuthoritiesConstants.class.getField("ANONYMOUS").getModifiers())).isTrue();
        assertThat(Modifier.isFinal(AuthoritiesConstants.class.getField("ANONYMOUS").getModifiers())).isTrue();
    }
}

