package com.ambry.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

@DisplayName("SecurityUtils Unit Tests")
class SecurityUtilsTest {

    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should get current user login from UserDetails")
    void testGetCurrentUserLoginFromUserDetails() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<String> result = SecurityUtils.getCurrentUserLogin();

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should get current user login from Jwt")
    void testGetCurrentUserLoginFromJwt() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("jwtuser");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<String> result = SecurityUtils.getCurrentUserLogin();

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo("jwtuser");
    }

    @Test
    @DisplayName("Should get current user login from String principal")
    void testGetCurrentUserLoginFromString() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("stringuser");
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<String> result = SecurityUtils.getCurrentUserLogin();

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo("stringuser");
    }

    @Test
    @DisplayName("Should return empty when authentication is null")
    void testGetCurrentUserLoginWhenAuthenticationIsNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        Optional<String> result = SecurityUtils.getCurrentUserLogin();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty when principal is unknown type")
    void testGetCurrentUserLoginWithUnknownPrincipalType() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new Object());
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<String> result = SecurityUtils.getCurrentUserLogin();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should get current user JWT when credentials is String")
    void testGetCurrentUserJWTWithStringCredentials() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getCredentials()).thenReturn("jwt-token-string");
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<String> result = SecurityUtils.getCurrentUserJWT();

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo("jwt-token-string");
    }

    @Test
    @DisplayName("Should return empty when credentials is not String")
    void testGetCurrentUserJWTWithNonStringCredentials() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getCredentials()).thenReturn(new Object());
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<String> result = SecurityUtils.getCurrentUserJWT();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty when authentication is null for JWT")
    void testGetCurrentUserJWTWhenAuthenticationIsNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        Optional<String> result = SecurityUtils.getCurrentUserJWT();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return true when user is authenticated")
    void testIsAuthenticated() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.isAuthenticated();

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when user has ANONYMOUS authority")
    void testIsAuthenticatedWithAnonymous() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS);
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.isAuthenticated();

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return false when authentication is null")
    void testIsAuthenticatedWhenNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        boolean result = SecurityUtils.isAuthenticated();

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when user has any of the specified authorities")
    void testHasCurrentUserAnyOfAuthorities() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.hasCurrentUserAnyOfAuthorities("ROLE_ADMIN", "ROLE_USER");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when user does not have any of the specified authorities")
    void testHasCurrentUserAnyOfAuthoritiesFalse() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_GUEST");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.hasCurrentUserAnyOfAuthorities("ROLE_ADMIN", "ROLE_USER");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return false when authentication is null for hasAnyOfAuthorities")
    void testHasCurrentUserAnyOfAuthoritiesWhenNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        boolean result = SecurityUtils.hasCurrentUserAnyOfAuthorities("ROLE_ADMIN");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when user has none of the specified authorities")
    void testHasCurrentUserNoneOfAuthorities() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_GUEST");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.hasCurrentUserNoneOfAuthorities("ROLE_ADMIN", "ROLE_USER");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when user has one of the specified authorities")
    void testHasCurrentUserNoneOfAuthoritiesFalse() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.hasCurrentUserNoneOfAuthorities("ROLE_ADMIN", "ROLE_USER");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when user has the specified authority")
    void testHasCurrentUserThisAuthority() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when user does not have the specified authority")
    void testHasCurrentUserThisAuthorityFalse() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return false when authentication is null for hasThisAuthority")
    void testHasCurrentUserThisAuthorityWhenNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        boolean result = SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when user has multiple authorities including requested one")
    void testHasCurrentUserAnyOfAuthoritiesWithMultipleAuthorities() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority1 = new SimpleGrantedAuthority("ROLE_USER");
        GrantedAuthority authority2 = new SimpleGrantedAuthority("ROLE_ADMIN");
        Collection<? extends GrantedAuthority> authorities = Arrays.asList(authority1, authority2);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.hasCurrentUserAnyOfAuthorities("ROLE_ADMIN", "ROLE_GUEST");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return true when user has multiple authorities and none matches")
    void testHasCurrentUserNoneOfAuthoritiesWithMultipleAuthorities() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority1 = new SimpleGrantedAuthority("ROLE_GUEST");
        GrantedAuthority authority2 = new SimpleGrantedAuthority("ROLE_VISITOR");
        Collection<? extends GrantedAuthority> authorities = Arrays.asList(authority1, authority2);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.hasCurrentUserNoneOfAuthorities("ROLE_ADMIN", "ROLE_USER");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return true when user has empty authorities list")
    void testIsAuthenticatedWithEmptyAuthorities() {
        Authentication authentication = mock(Authentication.class);
        Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.isAuthenticated();

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return true when user has multiple authorities including ANONYMOUS")
    void testIsAuthenticatedWithMultipleAuthoritiesIncludingAnonymous() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority1 = new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS);
        GrantedAuthority authority2 = new SimpleGrantedAuthority("ROLE_USER");
        Collection<? extends GrantedAuthority> authorities = Arrays.asList(authority1, authority2);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.isAuthenticated();

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when user has any of multiple authorities")
    void testHasCurrentUserAnyOfAuthoritiesWithMultipleMatches() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.hasCurrentUserAnyOfAuthorities("ROLE_ADMIN", "ROLE_USER", "ROLE_GUEST");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when no authorities are provided")
    void testHasCurrentUserAnyOfAuthoritiesWithNoAuthorities() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.hasCurrentUserAnyOfAuthorities();

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should verify JWT_ALGORITHM constant")
    void testJwtAlgorithmConstant() {
        assertThat(SecurityUtils.JWT_ALGORITHM).isNotNull();
        assertThat(SecurityUtils.JWT_ALGORITHM.getName()).isEqualTo("HS512");
    }

    @Test
    @DisplayName("Should verify AUTHORITIES_KEY constant")
    void testAuthoritiesKeyConstant() {
        assertThat(SecurityUtils.AUTHORITIES_KEY).isEqualTo("auth");
    }
}

