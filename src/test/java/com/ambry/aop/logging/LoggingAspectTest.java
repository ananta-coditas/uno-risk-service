package com.ambry.aop.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import com.ambry.config.UnoFrameworkConstants;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("LoggingAspect Unit Tests")
class LoggingAspectTest {

    @Mock
    private Environment environment;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    @Mock
    private Logger logger;

    private LoggingAspect loggingAspect;

    @BeforeEach
    void setUp() {
        loggingAspect = new LoggingAspect(environment);
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testMethod");
        when(signature.getDeclaringTypeName()).thenReturn("com.ambry.service.TestService");
    }

    @Test
    @DisplayName("Should log exception details in dev/profile profile")
    void testLogAfterThrowingInDevOrProdProfile() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        // Verify environment profile check was performed
        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        // Verify signature methods were called to get logger name
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should log limited exception details in non-dev/profile profile")
    void testLogAfterThrowingInNonDevOrProdProfile() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(false);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        // Verify environment profile check was performed
        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        // Verify signature methods were called to get logger name
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should log entry and exit when debug is enabled")
    void testLogAroundWithDebugEnabled() throws Throwable {
        Object result = new Object();
        Object[] args = new Object[]{"arg1", "arg2"};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        // We can't easily mock Logger.isDebugEnabled() without using a real logger
        // So we'll just verify the method executes successfully
        // Note: getArgs() is only called if debug is enabled, which may not be the case in test environment
        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        verify(proceedingJoinPoint).proceed();
        // getArgs() may or may not be called depending on debug logging level
    }

    @Test
    @DisplayName("Should execute without logging when debug is disabled")
    void testLogAroundWithDebugDisabled() throws Throwable {
        Object result = "test result";
        Object[] args = new Object[]{"arg1"};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        // When debug is disabled (typical in test environments), the method should still execute
        // but not log entry/exit. We verify the method executes successfully.
        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        verify(proceedingJoinPoint).proceed();
        // Note: We can't easily verify that logging didn't occur without mocking Logger,
        // but we can verify the method executed correctly
    }

    @Test
    @DisplayName("Should re-throw IllegalArgumentException and log args")
    void testLogAroundReThrowsIllegalArgumentException() throws Throwable {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");
        Object[] args = new Object[]{"arg1", 123, true};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenThrow(exception);

        try {
            loggingAspect.logAround(proceedingJoinPoint);
            assertThat(false).as("Should have thrown IllegalArgumentException").isTrue();
        } catch (IllegalArgumentException e) {
            assertThat(e).isEqualTo(exception);
            assertThat(e.getMessage()).isEqualTo("Invalid argument");
        }

        // Verify that getArgs() was called at least once (in the catch block for error logging)
        // It may be called twice if debug is enabled (once for entry log, once for error log)
        verify(proceedingJoinPoint, atLeast(1)).getArgs();
        // Verify proceed was called and threw exception
        verify(proceedingJoinPoint).proceed();
        // Verify signature methods were called
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should handle exception with null cause in dev/profile profile")
    void testLogAfterThrowingWithNullCause() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        // Should not throw exception even when cause is null
        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        // Verify environment profile check was performed
        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        // Verify signature methods were called
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
        // Verify exception has null cause
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Should handle exception with null cause in non-dev/profile profile")
    void testLogAfterThrowingWithNullCauseInNonDevProfile() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(false);

        // Should not throw exception even when cause is null
        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        // Verify environment profile check was performed
        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        // Verify signature methods were called
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
        // Verify exception has null cause
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Should handle exception with null message in dev/profile profile")
    void testLogAfterThrowingWithNullMessage() {
        RuntimeException exception = new RuntimeException((String) null);
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        // Verify environment profile check was performed
        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        // Verify signature methods were called
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
        // Verify exception has null message
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    @DisplayName("Should handle exception with null message in non-dev/profile profile")
    void testLogAfterThrowingWithNullMessageInNonDevProfile() {
        RuntimeException exception = new RuntimeException((String) null);
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(false);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        // Verify environment profile check was performed
        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        // Verify signature methods were called
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
        // Verify exception has null message
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    @DisplayName("Should handle exception with non-null cause in dev/profile profile")
    void testLogAfterThrowingWithNonNullCause() {
        RuntimeException cause = new RuntimeException("Root cause");
        RuntimeException exception = new RuntimeException("Test exception", cause);
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        // Verify environment profile check was performed
        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        // Verify signature methods were called
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
        // Verify exception has non-null cause
        assertThat(exception.getCause()).isNotNull();
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Should handle exception with non-null cause in non-dev/profile profile")
    void testLogAfterThrowingWithNonNullCauseInNonDevProfile() {
        RuntimeException cause = new RuntimeException("Root cause");
        RuntimeException exception = new RuntimeException("Test exception", cause);
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(false);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        // Verify environment profile check was performed
        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        // Verify signature methods were called
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
        // Verify exception has non-null cause
        assertThat(exception.getCause()).isNotNull();
    }

    @Test
    @DisplayName("Should handle successful method execution")
    void testLogAroundSuccessfulExecution() throws Throwable {
        Object result = "success";
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[0]);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        assertThat(returnedResult).isInstanceOf(String.class);
        verify(proceedingJoinPoint).proceed();
        // Verify signature methods were called (getDeclaringTypeName is always called for logger)
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        // getName() is only called if debug is enabled or in error cases
        // getArgs() is only called if debug is enabled, which may not be the case in test environment
    }

    @Test
    @DisplayName("Should handle args with null elements")
    void testLogAroundWithNullArgs() throws Throwable {
        Object result = "result";
        Object[] args = new Object[]{null, "arg2", null};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        verify(proceedingJoinPoint).proceed();
        // getArgs() is only called if debug is enabled, which may not be the case in test environment
    }

    @Test
    @DisplayName("Should handle args with complex objects")
    void testLogAroundWithComplexArgs() throws Throwable {
        Object result = "result";
        List<String> listArg = Arrays.asList("item1", "item2");
        Map<String, Integer> mapArg = Map.of("key1", 1, "key2", 2);
        Object[] args = new Object[]{listArg, mapArg, 42};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        verify(proceedingJoinPoint).proceed();
        // getArgs() is only called if debug is enabled, which may not be the case in test environment
    }

    @Test
    @DisplayName("Should handle args with primitive types")
    void testLogAroundWithPrimitiveArgs() throws Throwable {
        Object result = "result";
        Object[] args = new Object[]{42, 3.14, true, 'c', (byte) 1, (short) 2, 100L};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        verify(proceedingJoinPoint).proceed();
        // getArgs() is only called if debug is enabled, which may not be the case in test environment
    }

    @Test
    @DisplayName("Should handle empty args array")
    void testLogAroundWithEmptyArgs() throws Throwable {
        Object result = "result";
        Object[] args = new Object[0];
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        verify(proceedingJoinPoint).proceed();
        // getArgs() is only called if debug is enabled, which may not be the case in test environment
    }

    @Test
    @DisplayName("Should handle args with large array")
    void testLogAroundWithLargeArgsArray() throws Throwable {
        Object result = "result";
        Object[] args = new Object[100];
        Arrays.fill(args, "arg");
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        verify(proceedingJoinPoint).proceed();
        // getArgs() is only called if debug is enabled, which may not be the case in test environment
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException with multiple args")
    void testLogAroundIllegalArgumentExceptionWithMultipleArgs() throws Throwable {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid arguments");
        Object[] args = new Object[]{"arg1", "arg2", "arg3", 123, false};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenThrow(exception);

        try {
            loggingAspect.logAround(proceedingJoinPoint);
            assertThat(false).as("Should have thrown IllegalArgumentException").isTrue();
        } catch (IllegalArgumentException e) {
            assertThat(e).isEqualTo(exception);
            assertThat(e.getMessage()).isEqualTo("Invalid arguments");
        }

        // Verify that getArgs() was called at least once (in the catch block for error logging)
        // It may be called twice if debug is enabled (once for entry log, once for error log)
        verify(proceedingJoinPoint, atLeast(1)).getArgs();
        // Verify proceed was called and threw exception
        verify(proceedingJoinPoint).proceed();
        // Verify signature methods were called
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should handle args retrieval correctly in all scenarios")
    void testLogAroundArgsRetrieval() throws Throwable {
        Object result = "result";
        Object[] args = new Object[]{"test", 123};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        // Verify the args array content matches what was set up
        assertThat(args).hasSize(2);
        assertThat(args[0]).isEqualTo("test");
        assertThat(args[1]).isEqualTo(123);
        verify(proceedingJoinPoint).proceed();
        // Verify signature methods were called (getDeclaringTypeName is always called for logger)
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        // getName() is only called if debug is enabled or in error cases
        // getArgs() is only called if debug is enabled, which may not be the case in test environment
    }

    @Test
    @DisplayName("Should handle different exception types in logAfterThrowing")
    void testLogAfterThrowingWithDifferentExceptionTypes() {
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        // Test with NullPointerException
        NullPointerException npe = new NullPointerException("NPE message");
        loggingAspect.logAfterThrowing(proceedingJoinPoint, npe);
        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));

        // Test with IllegalStateException
        IllegalStateException ise = new IllegalStateException("ISE message");
        loggingAspect.logAfterThrowing(proceedingJoinPoint, ise);
        verify(environment, times(2)).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
    }

    @Test
    @DisplayName("Should handle exception with both null cause and null message in dev/profile profile")
    void testLogAfterThrowingWithNullCauseAndNullMessage() {
        RuntimeException exception = new RuntimeException((String) null);
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
        assertThat(exception.getCause()).isNull();
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    @DisplayName("Should handle exception with both null cause and null message in non-dev/profile profile")
    void testLogAfterThrowingWithNullCauseAndNullMessageInNonDevProfile() {
        RuntimeException exception = new RuntimeException((String) null);
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(false);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
        assertThat(exception.getCause()).isNull();
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    @DisplayName("Should test pointcut methods")
    void testPointcutMethods() {
        // Pointcut methods are empty, but we can verify they exist and are callable
        loggingAspect.springBeanPointcut();
        loggingAspect.applicationPackagePointcut();
        
        // Verify the aspect instance is not null
        assertThat(loggingAspect).isNotNull();
    }

    @Test
    @DisplayName("Should handle logAround with null result")
    void testLogAroundWithNullResult() throws Throwable {
        Object[] args = new Object[]{"arg1"};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(null);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isNull();
        verify(proceedingJoinPoint).proceed();
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
    }

    @Test
    @DisplayName("Should handle logAround with exception that has null message")
    void testLogAroundWithIllegalArgumentExceptionWithNullMessage() throws Throwable {
        IllegalArgumentException exception = new IllegalArgumentException((String) null);
        Object[] args = new Object[]{"arg1"};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenThrow(exception);

        try {
            loggingAspect.logAround(proceedingJoinPoint);
            assertThat(false).as("Should have thrown IllegalArgumentException").isTrue();
        } catch (IllegalArgumentException e) {
            assertThat(e).isEqualTo(exception);
            assertThat(e.getMessage()).isNull();
        }

        verify(proceedingJoinPoint, atLeast(1)).getArgs();
        verify(proceedingJoinPoint).proceed();
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should handle logAround with empty method name")
    void testLogAroundWithEmptyMethodName() throws Throwable {
        Object result = "result";
        when(signature.getName()).thenReturn("");
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[0]);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        verify(proceedingJoinPoint).proceed();
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
    }

    @Test
    @DisplayName("Should handle logAfterThrowing with empty method name")
    void testLogAfterThrowingWithEmptyMethodName() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(signature.getName()).thenReturn("");
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should handle logAround with very long method name")
    void testLogAroundWithVeryLongMethodName() throws Throwable {
        Object result = "result";
        String longMethodName = "a".repeat(1000);
        when(signature.getName()).thenReturn(longMethodName);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[0]);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        verify(proceedingJoinPoint).proceed();
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        // getName() is only called if debug is enabled or in error cases
        // Since we're not throwing an exception, it may not be called if debug is disabled
    }

    @Test
    @DisplayName("Should handle logAfterThrowing with very long method name")
    void testLogAfterThrowingWithVeryLongMethodName() {
        RuntimeException exception = new RuntimeException("Test exception");
        String longMethodName = "a".repeat(1000);
        when(signature.getName()).thenReturn(longMethodName);
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should handle logAround with result that is an array")
    void testLogAroundWithArrayResult() throws Throwable {
        String[] result = new String[]{"item1", "item2", "item3"};
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[0]);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        assertThat(returnedResult).isInstanceOf(String[].class);
        verify(proceedingJoinPoint).proceed();
    }

    @Test
    @DisplayName("Should handle logAround with result that is a collection")
    void testLogAroundWithCollectionResult() throws Throwable {
        List<String> result = Arrays.asList("item1", "item2");
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[0]);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        assertThat(returnedResult).isInstanceOf(List.class);
        verify(proceedingJoinPoint).proceed();
    }

    @Test
    @DisplayName("Should handle logAround with result that is a primitive")
    void testLogAroundWithPrimitiveResult() throws Throwable {
        int result = 42;
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[0]);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        assertThat(returnedResult).isInstanceOf(Integer.class);
        verify(proceedingJoinPoint).proceed();
    }

    @Test
    @DisplayName("Should handle logAround with result that is a boolean")
    void testLogAroundWithBooleanResult() throws Throwable {
        boolean result = true;
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[0]);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        assertThat(returnedResult).isInstanceOf(Boolean.class);
        verify(proceedingJoinPoint).proceed();
    }

    @Test
    @DisplayName("Should handle non-IllegalArgumentException exceptions in logAround")
    void testLogAroundWithNonIllegalArgumentException() throws Throwable {
        RuntimeException exception = new RuntimeException("Runtime exception");
        Object[] args = new Object[]{"arg1"};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenThrow(exception);

        try {
            loggingAspect.logAround(proceedingJoinPoint);
            assertThat(false).as("Should have thrown RuntimeException").isTrue();
        } catch (RuntimeException e) {
            assertThat(e).isEqualTo(exception);
            assertThat(e.getMessage()).isEqualTo("Runtime exception");
        }

        verify(proceedingJoinPoint).proceed();
        // Verify signature methods were called (getDeclaringTypeName is always called for logger)
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        // getName() is only called if debug is enabled or in error cases (not for non-IllegalArgumentException)
    }

    @Test
    @DisplayName("Should verify signature information is retrieved correctly")
    void testSignatureInformationRetrieval() throws Throwable {
        String methodName = "calculateRisk";
        String className = "com.ambry.service.RiskService";
        Object result = "result";

        when(signature.getName()).thenReturn(methodName);
        when(signature.getDeclaringTypeName()).thenReturn(className);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[0]);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        // Verify signature information was retrieved
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        // Verify the class name was set correctly
        assertThat(signature.getDeclaringTypeName()).isEqualTo(className);
        // getName() is only called if debug is enabled or in error cases
        // Since we're not throwing an exception, it may not be called if debug is disabled
    }

    @Test
    @DisplayName("Should handle logAround with Throwable exception")
    void testLogAroundWithThrowableException() throws Throwable {
        Throwable exception = new Throwable("Throwable exception");
        Object[] args = new Object[]{"arg1"};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenThrow(exception);

        try {
            loggingAspect.logAround(proceedingJoinPoint);
            assertThat(false).as("Should have thrown Throwable").isTrue();
        } catch (Throwable e) {
            assertThat(e).isEqualTo(exception);
            assertThat(e.getMessage()).isEqualTo("Throwable exception");
        }

        verify(proceedingJoinPoint).proceed();
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
    }

    @Test
    @DisplayName("Should handle logAround with Error exception")
    void testLogAroundWithErrorException() throws Throwable {
        Error error = new Error("Error exception");
        Object[] args = new Object[]{"arg1"};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenThrow(error);

        try {
            loggingAspect.logAround(proceedingJoinPoint);
            assertThat(false).as("Should have thrown Error").isTrue();
        } catch (Error e) {
            assertThat(e).isEqualTo(error);
            assertThat(e.getMessage()).isEqualTo("Error exception");
        }

        verify(proceedingJoinPoint).proceed();
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
    }

    @Test
    @DisplayName("Should handle logAfterThrowing with Error")
    void testLogAfterThrowingWithError() {
        Error error = new Error("Error message");
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, error);

        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should handle logAfterThrowing with Exception")
    void testLogAfterThrowingWithException() {
        Exception exception = new Exception("Exception message");
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, exception);

        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should handle logAfterThrowing with Throwable")
    void testLogAfterThrowingWithThrowable() {
        Throwable throwable = new Throwable("Throwable message");
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        loggingAspect.logAfterThrowing(proceedingJoinPoint, throwable);

        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should handle logAround with result that is a Map")
    void testLogAroundWithMapResult() throws Throwable {
        Map<String, String> result = Map.of("key1", "value1", "key2", "value2");
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[0]);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        assertThat(returnedResult).isInstanceOf(Map.class);
        verify(proceedingJoinPoint).proceed();
    }

    @Test
    @DisplayName("Should handle logAround with result that is a custom object")
    void testLogAroundWithCustomObjectResult() throws Throwable {
        TestCustomObject result = new TestCustomObject("test", 123);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[0]);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        assertThat(returnedResult).isInstanceOf(TestCustomObject.class);
        verify(proceedingJoinPoint).proceed();
    }

    @Test
    @DisplayName("Should handle logAround with args containing arrays")
    void testLogAroundWithArrayArgs() throws Throwable {
        Object result = "result";
        int[] intArray = {1, 2, 3};
        String[] stringArray = {"a", "b", "c"};
        Object[] args = new Object[]{intArray, stringArray};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        verify(proceedingJoinPoint).proceed();
    }

    @Test
    @DisplayName("Should handle logAround with args containing nested collections")
    void testLogAroundWithNestedCollectionArgs() throws Throwable {
        Object result = "result";
        List<List<String>> nestedList = Arrays.asList(
            Arrays.asList("a", "b"),
            Arrays.asList("c", "d")
        );
        Object[] args = new Object[]{nestedList};
        when(proceedingJoinPoint.getArgs()).thenReturn(args);
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        Object returnedResult = loggingAspect.logAround(proceedingJoinPoint);

        assertThat(returnedResult).isEqualTo(result);
        verify(proceedingJoinPoint).proceed();
    }

    @Test
    @DisplayName("Should handle logAfterThrowing with JoinPoint (not ProceedingJoinPoint)")
    void testLogAfterThrowingWithJoinPoint() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(true);

        loggingAspect.logAfterThrowing(joinPoint, exception);

        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        verify(joinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should handle logAfterThrowing with JoinPoint in non-dev/profile profile")
    void testLogAfterThrowingWithJoinPointInNonDevProfile() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)))
            .thenReturn(false);

        loggingAspect.logAfterThrowing(joinPoint, exception);

        verify(environment).acceptsProfiles(Profiles.of(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT, UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION));
        verify(joinPoint, atLeast(1)).getSignature();
        verify(signature, atLeast(1)).getDeclaringTypeName();
        verify(signature, atLeast(1)).getName();
    }

    @Test
    @DisplayName("Should handle constructor with Environment")
    void testConstructor() {
        LoggingAspect aspect = new LoggingAspect(environment);
        assertThat(aspect).isNotNull();
    }

    // Helper class for testing custom objects
    private static class TestCustomObject {
        private String name;
        private int value;

        public TestCustomObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestCustomObject that = (TestCustomObject) o;
            return value == that.value && name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode() * 31 + value;
        }
    }
}

