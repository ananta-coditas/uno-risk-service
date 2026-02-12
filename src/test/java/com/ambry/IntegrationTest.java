package com.ambry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;

import com.ambry.config.AsyncSyncConfiguration;
import com.ambry.config.EmbeddedSQL;
import com.ambry.config.TestMeterRegistryConfiguration;
import com.ambry.config.TestSecretsManagerConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { CommonRiskServiceApp.class, AsyncSyncConfiguration.class, TestMeterRegistryConfiguration.class, TestSecretsManagerConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
