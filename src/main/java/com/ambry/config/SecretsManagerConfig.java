package com.ambry.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

/**
 * Configuration class for Secrets Manager client.
 * 
 * @author uno
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties(SecretsManagerProperties.class)
public class SecretsManagerConfig {

    private static final Logger log = LoggerFactory.getLogger(SecretsManagerConfig.class);

    @Value("${aws.region:us-west-2}")
    private String awsRegion;
 
    @Value("${spring.security.oauth2.resourceserver.jwt.secretname}")
    private String secretName;

    @Autowired
    private SecretsManagerProperties secretsManagerProperties;

    /**
     * Configures SecretsManagerProperties with the secret name from application configuration.
     */
    @PostConstruct
    public void configureSecretsManagerProperties() {
        if (secretName != null && !secretName.trim().isEmpty()) {
            secretsManagerProperties.setSecretName(secretName);
            log.info("Configured SecretsManagerProperties with secret name: {}", secretName);
        }
    }

    /**
     * Creates and configures the AWS Secrets Manager client.
     * 
     * @return SecretsManagerClient instance
     */
    @Bean
    public SecretsManagerClient secretsManagerClient() {
        log.info("Initializing AWS Secrets Manager client for region: {}", awsRegion);
        
        return SecretsManagerClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}

