package com.ambry.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

public class SecretsManagerConfig {

    private static final Logger log = LoggerFactory.getLogger(SecretsManagerConfig.class);

    @Value("${aws.region:us-west-2}")
    private String awsRegion;

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

