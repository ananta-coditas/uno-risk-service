package com.ambry.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Secrets Manager.
 * 
 * @author uno
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "aws.secretsmanager")
public class SecretsManagerProperties {
    
    private boolean enabled = true;
    private int timeout = 30000; // 30 seconds default
    private int maxRetries = 3;
    private long retryDelayMs = 1000;
    private String secretName;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public long getRetryDelayMs() {
        return retryDelayMs;
    }

    public void setRetryDelayMs(long retryDelayMs) {
        this.retryDelayMs = retryDelayMs;
    }

    public String getSecretName() {
        return secretName;
    }

    public void setSecretName(String secretName) {
        this.secretName = secretName;
    }
}

