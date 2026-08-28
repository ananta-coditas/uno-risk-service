package com.ambry.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.ambry.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final ApplicationProperties applicationProperties;
    private final UnoFrameworkConfig unoFrameworkConfigProperties;

    public SecurityConfiguration(ApplicationProperties applicationProperties, UnoFrameworkConfig unoFrameworkConfigProperties) {
        this.applicationProperties = applicationProperties;
        this.unoFrameworkConfigProperties = unoFrameworkConfigProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz ->
                // prettier-ignore
                authz
                    .requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/authenticate").permitAll()
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/api/register").permitAll()
                    .requestMatchers("/api/activate").permitAll()
                    .requestMatchers("/api/account/reset-password/init").permitAll()
                    .requestMatchers("/api/account/reset-password/finish").permitAll()
                    .requestMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .requestMatchers("/risk/api/models/swagger-ui/**").permitAll()
                    .requestMatchers("/risk/api/models/api-docs/**").permitAll()
                    .requestMatchers("/resources/**").permitAll()
                    .requestMatchers("/calculate/**").authenticated()
                    .requestMatchers("/bayescalculate/**").permitAll()
                    .requestMatchers("/risk/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .requestMatchers(applicationProperties.getPublicPath() == null ? "/risk/version" : applicationProperties.getPublicPath()).permitAll()
                    .requestMatchers("/risk/api/*/models/**").authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions ->
                exceptions
                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
        return http.build();
    }
}
