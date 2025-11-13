package com.ambry.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.Executor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;

/**
 * Unit tests for {@link AsyncConfiguration}.
 */
class AsyncConfigurationTest {

    private AsyncConfiguration asyncConfiguration;
    private TaskExecutionProperties taskExecutionProperties;

    @BeforeEach
    void setUp() {
        taskExecutionProperties = new TaskExecutionProperties();
        // TaskExecutionProperties uses getPool() which returns a Pool object
        // We'll use the default pool settings or configure via properties
        taskExecutionProperties.setThreadNamePrefix("test-async-");

        asyncConfiguration = new AsyncConfiguration(taskExecutionProperties);
    }

    @Test
    void testGetAsyncExecutor() {
        Executor executor = asyncConfiguration.getAsyncExecutor();
        assertThat(executor).isNotNull();
    }

    @Test
    void testGetAsyncUncaughtExceptionHandler() {
        AsyncUncaughtExceptionHandler handler = asyncConfiguration.getAsyncUncaughtExceptionHandler();
        assertThat(handler).isNotNull();
    }

    @Test
    void testAsyncExecutorIsTaskExecutor() {
        Executor executor = asyncConfiguration.getAsyncExecutor();
        assertThat(executor).isInstanceOf(com.ambry.async.ExceptionHandlingAsyncTaskExecutor.class);
    }
}

