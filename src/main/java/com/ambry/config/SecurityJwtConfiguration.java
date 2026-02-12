package com.ambry.config;

import com.ambry.management.SecurityMetersService;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import java.text.ParseException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class SecurityJwtConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityJwtConfiguration.class);

    // @Value("${uno.security.authentication.jwt.base64-secret}")
    // private String jwtKey;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetKeyUri;

    @Value("${uno.security.authentication.jwt.secretname:}")
    private String secretName;

    private final SecretsManager secretsManagerService;

    public SecurityJwtConfiguration(SecretsManager secretsManagerService) {
        this.secretsManagerService = secretsManagerService;
    }

    @Bean
    public JwtDecoder jwtDecoder(SecurityMetersService metersService) {
        if (secretName != null && !secretName.isEmpty()) {
            Map<String, String> credentials = secretsManagerService.getOAuthCredentials(secretName);

            // Create a decoder for each JWK Set URI
            String progenyJwkSetUri = credentials.get("progeny-jwk-set-uri");
            String progenyIssuerUri = credentials.get("progeny-issuer-uri");
            NimbusJwtDecoder progenyDecoder = NimbusJwtDecoder.withJwkSetUri(progenyJwkSetUri).build();

            String careJwkSetUri = credentials.get("care-jwk-set-uri");
            String careIssuerUri = credentials.get("care-issuer-uri");
            NimbusJwtDecoder careDecoder = NimbusJwtDecoder.withJwkSetUri(careJwkSetUri).build();

            LOG.info("Configured multi-issuer JWT decoder with progeny issuer: {} and care issuer: {}", progenyIssuerUri, careIssuerUri);

            // Create wrapped decoders with metrics tracking
            JwtDecoder wrappedProgenyDecoder = wrapDecoder(progenyDecoder, metersService);
            JwtDecoder wrappedCareDecoder = wrapDecoder(careDecoder, metersService);

            return token -> {
                // Extract issuer claim from JWT without full validation for optimal routing
                try {
                    JWT unverifiedJwt = JWTParser.parse(token);
                    String issuer = unverifiedJwt.getJWTClaimsSet().getIssuer();

                    // Route to appropriate decoder based on issuer claim
                    if (progenyIssuerUri != null && progenyIssuerUri.equals(issuer)) {
                        LOG.debug("Routing token to progeny decoder based on issuer: {}", issuer);
                        return wrappedProgenyDecoder.decode(token);
                    } else if (careIssuerUri != null && careIssuerUri.equals(issuer)) {
                        LOG.debug("Routing token to care decoder based on issuer: {}", issuer);
                        return wrappedCareDecoder.decode(token);
                    } else {
                        LOG.warn("Token issuer '{}' does not match any configured issuer (progeny: {}, care: {})", 
                                 issuer, progenyIssuerUri, careIssuerUri);
                        // Fallback to sequential decoding if issuer doesn't match
                        return tryBothDecoders(token, wrappedProgenyDecoder, wrappedCareDecoder);
                    }
                } catch (ParseException e) {
                    LOG.debug("Failed to parse JWT token to extract issuer, falling back to sequential decoding", e);
                    // Fallback to sequential decoding if parsing fails
                    return tryBothDecoders(token, wrappedProgenyDecoder, wrappedCareDecoder);
                }
            };
        } else {
            // Fallback to single decoder when secretName is not configured
            LOG.info("Using single JWT decoder with jwk-set-uri: {}", jwkSetKeyUri);
            NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetKeyUri).build();
            return wrapDecoder(jwtDecoder, metersService);
        }
    }

    /**
     * Fallback method to try both decoders sequentially when issuer-based routing fails.
     * This ensures backward compatibility and handles edge cases.
     */
    private Jwt tryBothDecoders(String token, JwtDecoder progenyDecoder, JwtDecoder careDecoder) {
        // Try progeny decoder first
        try {
            return progenyDecoder.decode(token);
        } catch (Exception e1) {
            LOG.debug("Failed to decode with progeny decoder, trying care decoder", e1);
            // Try care decoder as fallback
            try {
                return careDecoder.decode(token);
            } catch (Exception e2) {
                LOG.debug("Failed to decode with care decoder", e2);
                // Re-throw the last exception (from care decoder)
                throw e2;
            }
        }
    }

    private JwtDecoder wrapDecoder(JwtDecoder jwtDecoder, SecurityMetersService metersService) {
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                String errorMessage = e.getMessage();
                if (errorMessage != null) {
                    if (errorMessage.contains("Invalid signature")) {
                        metersService.trackTokenInvalidSignature();
                    } else if (errorMessage.contains("Jwt expired at")) {
                        metersService.trackTokenExpired();
                    } else if (
                        errorMessage.contains("Invalid JWT serialization") ||
                        errorMessage.contains("Malformed token") ||
                        errorMessage.contains("Invalid unsecured/JWS/JWE")
                    ) {
                        metersService.trackTokenMalformed();
                    } else {
                        LOG.error("Unknown JWT error: {}", errorMessage);
                    }
                } else {
                    LOG.error("JWT decoding failed with unknown error", e);
                }
                throw e;
            }
        };
    }

    // @Bean
    // public JwtEncoder jwtEncoder() {
    //     return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    // }

    // private SecretKey getSecretKey() {
    //     byte[] keyBytes = Base64.from(jwtKey).decode();
    //     return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    // }
}
