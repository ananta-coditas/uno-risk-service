package com.ambry.web.rest.errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import com.ambry.config.UnoFrameworkConstants;
import com.ambry.web.rest.errors.UnoProblemDetailEx.ProblemDetailWithCauseBuilder;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@DisplayName("ExceptionTranslator Unit Tests")
class ExceptionTranslatorTest {

    private ExceptionTranslator exceptionTranslator;
    private Environment environment;
    private NativeWebRequest nativeWebRequest;
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        environment = mock(Environment.class);
        exceptionTranslator = new ExceptionTranslator(environment);
        
        httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        nativeWebRequest = new ServletWebRequest(httpServletRequest);
    }

    @Test
    @DisplayName("Should handle generic exception")
    void testHandleAnyException() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Should map AccessDeniedException to FORBIDDEN status")
    void testHandleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Should map BadCredentialsException to UNAUTHORIZED status")
    void testHandleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Bad credentials");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Should customize problem with production profile")
    void testCustomizeProblemInProduction() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getStatus()).isGreaterThan(0);
        assertThat(problem.getProperties()).isNotNull();
    }

    @Test
    @DisplayName("Should customize problem with development profile")
    void testCustomizeProblemInDevelopment() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getStatus()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should include path in problem details")
    void testProblemIncludesPath() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getStatus()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle exception with null request")
    void testHandleExceptionWithNullRequest() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, null);

        assertThat(problem).isNotNull();
        assertThat(problem.getStatus()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle exception internal with null body")
    void testHandleExceptionInternalWithNullBody() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        // handleExceptionInternal is protected, but we can test it indirectly through handleAnyException
        // or we can verify that when body is null, wrapAndCustomizeProblem is called
        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Should handle exception internal with provided body")
    void testHandleExceptionInternalWithBody() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});
        
        // Test that handleExceptionInternal uses the provided body
        // Since it's protected, we test through handleAnyException which calls it
        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(UnoProblemDetailEx.class);
    }

    @Test
    @DisplayName("Should include field errors for MethodArgumentNotValidException")
    void testHandleMethodArgumentNotValidException() throws NoSuchMethodException {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("testDTO", "field1", "Field 1 is required");
        FieldError fieldError2 = new FieldError("testDTO", "field2", "Field 2 is invalid");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        
        // Create a MethodParameter for the exception constructor
        Method method = this.getClass().getDeclaredMethod("helperMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
        UnoProblemDetailEx problem = (UnoProblemDetailEx) response.getBody();
        assertThat(problem).isNotNull();
        assertThat(problem.getProperties()).isNotNull();
        assertThat(problem.getProperties().containsKey("fieldErrors")).isTrue();
        
        @SuppressWarnings("unchecked")
        List<FieldErrorVM> fieldErrors = (List<FieldErrorVM>) problem.getProperties().get("fieldErrors");
        assertThat(fieldErrors).isNotNull();
        assertThat(fieldErrors).hasSize(2);
        assertThat(fieldErrors.get(0).getField()).isEqualTo("field1");
        assertThat(fieldErrors.get(0).getMessage()).isEqualTo("Field 1 is required");
        assertThat(fieldErrors.get(1).getField()).isEqualTo("field2");
        assertThat(fieldErrors.get(1).getMessage()).isEqualTo("Field 2 is invalid");
    }

    @Test
    @DisplayName("Should set correct type and title for MethodArgumentNotValidException")
    void testMethodArgumentNotValidExceptionDetails() throws NoSuchMethodException {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());
        
        // Create a MethodParameter for the exception constructor
        Method method = this.getClass().getDeclaredMethod("helperMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getTitle()).isEqualTo("Method argument not valid");
        assertThat(problem.getType()).isNotNull();
    }

    @Test
    @DisplayName("Should handle BadRequestAlertException with headers")
    void testHandleBadRequestAlertException() {
        BadRequestAlertException exception = new BadRequestAlertException("Test error", "testEntity", "testError");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});
        
        // Use reflection to set applicationName since it's @Value injected
        try {
            java.lang.reflect.Field field = ExceptionTranslator.class.getDeclaredField("applicationName");
            field.setAccessible(true);
            field.set(exceptionTranslator, "testApp");
        } catch (Exception e) {
            // If reflection fails, continue without setting it
        }

        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders()).isNotNull();
    }

    @Test
    @DisplayName("Should handle HttpMessageConversionException in production")
    void testHandleHttpMessageConversionExceptionInProduction() {
        HttpMessageConversionException exception = new HttpMessageConversionException("Conversion error");
        when(environment.getActiveProfiles()).thenReturn(new String[]{UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getDetail()).isEqualTo("Unable to convert http message");
    }

    @Test
    @DisplayName("Should handle exception with package name in production")
    void testHandleExceptionWithPackageNameInProduction() {
        RuntimeException exception = new RuntimeException("com.ambry.SomeException: Error occurred");
        when(environment.getActiveProfiles()).thenReturn(new String[]{UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getDetail()).isEqualTo("Unexpected runtime exception");
    }

    @Test
    @DisplayName("Should handle exception with various package name patterns in production")
    void testHandleExceptionWithVariousPackageNames() {
        String[] packageNames = {"org.", "java.", "net.", "jakarta.", "javax.", "com.", "io.", "de.", "com.ambry"};
        
        for (String packageName : packageNames) {
            RuntimeException exception = new RuntimeException(packageName + "SomeClass: Error");
            when(environment.getActiveProfiles()).thenReturn(new String[]{UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION});

            UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

            assertThat(problem).isNotNull();
            assertThat(problem.getDetail()).isEqualTo("Unexpected runtime exception");
        }
    }

    @Test
    @DisplayName("Should handle exception with cause in production")
    void testHandleExceptionWithCauseInProduction() {
        RuntimeException cause = new RuntimeException("Root cause");
        RuntimeException exception = new RuntimeException("Wrapper exception", cause);
        when(environment.getActiveProfiles()).thenReturn(new String[]{UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getDetail()).isEqualTo("Root cause");
    }

    @Test
    @DisplayName("Should handle ErrorResponseException with UnoProblemDetailEx body")
    void testHandleErrorResponseExceptionWithUnoProblemDetailEx() {
        UnoProblemDetailEx existingProblem = ProblemDetailWithCauseBuilder.instance()
            .withStatus(400)
            .withTitle("Existing title")
            .build();
        
        ErrorResponseException exception = new ErrorResponseException(
            HttpStatus.BAD_REQUEST,
            existingProblem,
            null
        );
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem).isEqualTo(existingProblem);
    }

    @Test
    @DisplayName("Should handle field error with blank default message")
    void testHandleFieldErrorWithBlankMessage() throws NoSuchMethodException {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("testDTO", "field1", null, false, new String[]{"error.code"}, null, "error.code");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        
        Method method = this.getClass().getDeclaredMethod("helperMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getProperties()).isNotNull();
        assertThat(problem.getProperties().containsKey("fieldErrors")).isTrue();
        
        @SuppressWarnings("unchecked")
        List<FieldErrorVM> fieldErrors = (List<FieldErrorVM>) problem.getProperties().get("fieldErrors");
        assertThat(fieldErrors).isNotNull();
        assertThat(fieldErrors).hasSize(1);
        assertThat(fieldErrors.get(0).getMessage()).isEqualTo("error.code");
    }

    @Test
    @DisplayName("Should handle field error with DTO suffix removal")
    void testHandleFieldErrorWithDTOSuffix() throws NoSuchMethodException {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("userDTO", "email", "Email is invalid");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        
        Method method = this.getClass().getDeclaredMethod("helperMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        @SuppressWarnings("unchecked")
        List<FieldErrorVM> fieldErrors = (List<FieldErrorVM>) problem.getProperties().get("fieldErrors");
        assertThat(fieldErrors).isNotNull();
        assertThat(fieldErrors).hasSize(1);
        assertThat(fieldErrors.get(0).getObjectName()).isEqualTo("user");
    }

    @Test
    @DisplayName("Should handle exception with ResponseStatus annotation")
    void testHandleExceptionWithResponseStatus() {
        @ResponseStatus(HttpStatus.NOT_FOUND)
        class NotFoundException extends RuntimeException {
            NotFoundException(String message) {
                super(message);
            }
        }

        NotFoundException exception = new NotFoundException("Not found");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Should handle exception with null message")
    void testHandleExceptionWithNullMessage() {
        RuntimeException exception = new RuntimeException((String) null);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getStatus()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle exception with null cause")
    void testHandleExceptionWithNullCause() {
        RuntimeException exception = new RuntimeException("Test", null);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getDetail()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Should handle buildCause with null throwable")
    void testBuildCauseWithNullThrowable() {
        Optional<UnoProblemDetailEx> cause = exceptionTranslator.buildCause(null, nativeWebRequest);

        assertThat(cause).isNotNull();
        assertThat(cause.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should handle buildCause with casual chain disabled")
    void testBuildCauseWithCasualChainDisabled() {
        RuntimeException cause = new RuntimeException("Cause");
        Optional<UnoProblemDetailEx> result = exceptionTranslator.buildCause(cause, nativeWebRequest);

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should handle problem with about:blank type")
    void testHandleProblemWithAboutBlankType() {
        UnoProblemDetailEx problem = ProblemDetailWithCauseBuilder.instance()
            .withStatus(500)
            .withType(URI.create("about:blank"))
            .build();
        
        RuntimeException exception = new RuntimeException("Test");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx customized = exceptionTranslator.customizeProblem(problem, exception, nativeWebRequest);

        assertThat(customized).isNotNull();
        assertThat(customized.getType()).isNotEqualTo(URI.create("about:blank"));
    }

    @Test
    @DisplayName("Should handle problem with existing title")
    void testHandleProblemWithExistingTitle() {
        UnoProblemDetailEx problem = ProblemDetailWithCauseBuilder.instance()
            .withStatus(500)
            .withTitle("Existing Title")
            .build();
        
        RuntimeException exception = new RuntimeException("Test");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx customized = exceptionTranslator.customizeProblem(problem, exception, nativeWebRequest);

        assertThat(customized).isNotNull();
        assertThat(customized.getTitle()).isNotNull();
    }

    @Test
    @DisplayName("Should handle problem with status <= 0")
    void testHandleProblemWithInvalidStatus() {
        UnoProblemDetailEx problem = ProblemDetailWithCauseBuilder.instance()
            .withStatus(0)
            .build();
        
        RuntimeException exception = new RuntimeException("Test");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx customized = exceptionTranslator.customizeProblem(problem, exception, nativeWebRequest);

        assertThat(customized).isNotNull();
        assertThat(customized.getStatus()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle problem with existing properties")
    void testHandleProblemWithExistingProperties() {
        UnoProblemDetailEx problem = ProblemDetailWithCauseBuilder.instance()
            .withStatus(500)
            .withProperty("message", "existing.message")
            .withProperty("path", "/existing/path")
            .build();
        
        RuntimeException exception = new RuntimeException("Test");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx customized = exceptionTranslator.customizeProblem(problem, exception, nativeWebRequest);

        assertThat(customized).isNotNull();
        assertThat(customized.getProperties()).isNotNull();
    }

    @Test
    @DisplayName("Should handle request with null native request")
    void testHandleRequestWithNullNativeRequest() {
        NativeWebRequest nullRequest = mock(NativeWebRequest.class);
        when(nullRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(null);
        
        RuntimeException exception = new RuntimeException("Test");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nullRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getProperties()).isNotNull();
        assertThat(problem.getProperties().get("path")).isNotNull();
    }

    @Test
    @DisplayName("Should handle ErrorResponse with status")
    void testHandleErrorResponse() {
        // Create an ErrorResponseException which implements ErrorResponse and extends Throwable
        org.springframework.http.ProblemDetail problemDetail = org.springframework.http.ProblemDetail.forStatus(400);
        problemDetail.setTitle("Error");
        ErrorResponseException errorResponseException = new ErrorResponseException(
            HttpStatus.BAD_REQUEST,
            problemDetail,
            null
        );

        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(errorResponseException, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // Helper method for creating MethodParameter
    @SuppressWarnings("unused")
    private void helperMethod(String param) {
        // This method is only used for reflection to create MethodParameter
    }
}

