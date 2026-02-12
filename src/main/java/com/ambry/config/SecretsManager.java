package com.ambry.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for retrieving secrets from Secrets Manager.
 * 
 * @author uno
 * @version 1.0.0
 */
@Service
public class SecretsManager {
    private static final Logger log = LoggerFactory.getLogger(SecretsManager.class);
    private final SecretsManagerClient secretsManagerClient;
    private final SecretsManagerProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor for SecretsManager service.
     * 
     * @param secretsManagerClient The AWS Secrets Manager client
     * @param properties The Secrets Manager configuration properties
     */
    public SecretsManager(SecretsManagerClient secretsManagerClient, SecretsManagerProperties properties) {
        this.secretsManagerClient = secretsManagerClient;
        this.properties = properties;
    }

    /**
     * Retrieves a secret value from Secrets Manager by secret name.
     * 
     * @param secretName The name of the secret in Secrets Manager
     * @return The secret value as a string
     * @throws RuntimeException if the secret cannot be retrieved
     */
    public String getSecretValue(String secretName) {
        if (secretName == null || secretName.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret name cannot be null or empty");
        }

        // Warn if Secrets Manager is not explicitly enabled, but allow retrieval to proceed
        // since a secretName was explicitly provided
        if (!properties.isEnabled()) {
            log.warn("Secrets Manager is not explicitly enabled, but attempting to retrieve secret: {}", secretName);
        }

        log.info("Retrieving secret from Secrets Manager: {}", secretName);

        try {
            GetSecretValueRequest request = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);
            String secretString = response.secretString();

            log.info("Successfully retrieved secret: {}", secretName);
            return secretString;

        } catch (SecretsManagerException e) {
            log.error("Error retrieving secret '{}' from Secrets Manager: {}", secretName, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve secret from Secrets Manager: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves username and password from Secrets Manager.
     * The secret is expected to be a JSON object with "username" and "password" fields.
     * 
     * @param secretName The name of the secret in Secrets Manager
     * @return Map containing "username" and "password" keys
     * @throws RuntimeException if the secret cannot be retrieved or parsed
     */
    public Map<String, String> getOAuthCredentials(String secretName) {
        String secretValue = getSecretValue(secretName);
        
        try {
            JsonNode jsonNode = objectMapper.readTree(secretValue);
            
            Map<String, String> oAuthcredentials = new HashMap<>();
            
            if (jsonNode.has("progeny-jwk-set-uri")) {
                oAuthcredentials.put("progeny-jwk-set-uri", jsonNode.get("progeny-jwk-set-uri").asText());
            } else {
                throw new RuntimeException("Secret does not contain 'progeny-jwk-set-uri' field");
            }
            
            if (jsonNode.has("progeny-issuer-uri")) {
                oAuthcredentials.put("progeny-issuer-uri", jsonNode.get("progeny-issuer-uri").asText());
            } else {
                throw new RuntimeException("Secret does not contain 'progeny-issuer-uri' field");
            }
            if (jsonNode.has("care-jwk-set-uri")) {
                oAuthcredentials.put("care-jwk-set-uri", jsonNode.get("care-jwk-set-uri").asText());
            } else {
                throw new RuntimeException("Secret does not contain 'care-jwk-set-uri' field");
            }
            
            if (jsonNode.has("care-issuer-uri")) {
                oAuthcredentials.put("care-issuer-uri", jsonNode.get("care-issuer-uri").asText());
            } else {
                throw new RuntimeException("Secret does not contain 'care-issuer-uri' field");
            }
            
            log.info("Successfully parsed oAuth credentials from secret: {}", secretName);
            return oAuthcredentials;
            
        } catch (Exception e) {
            log.error("Error parsing secret value as JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to parse secret value as JSON: " + e.getMessage(), e);
        }
    }
}



