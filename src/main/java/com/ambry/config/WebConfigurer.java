package com.ambry.config;

import io.micrometer.tracing.Tracer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.*;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfigurer.class);

    private final Environment env;

    private final UnoFrameworkConfig unoFrameworkConfigProperties;

    @Autowired
    private Tracer tracer;

    public WebConfigurer(Environment env, UnoFrameworkConfig unoFrameworkConfigProperties) {
        this.env = env;
        this.unoFrameworkConfigProperties = unoFrameworkConfigProperties;
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        if (env.getActiveProfiles().length != 0) {
            LOG.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }

        LOG.info("Web application fully configured");
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = unoFrameworkConfigProperties.getCors();
        if (!CollectionUtils.isEmpty(config.getAllowedOrigins()) || !CollectionUtils.isEmpty(config.getAllowedOriginPatterns())) {
            LOG.debug("Registering CORS filter");
            source.registerCorsConfiguration("/", config);
            source.registerCorsConfiguration("/api/**", config);
            source.registerCorsConfiguration("/management/**", config);
            source.registerCorsConfiguration("/api/risk/models/api-docs/**", config);
            source.registerCorsConfiguration("/api/risk/models/swagger-ui/**", config);
        }
        return new CorsFilter(source);
    }
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 10)
    public OncePerRequestFilter customBaggageFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain) throws IOException, ServletException {

                // Get header "Source" (case-insensitive)
                String source = request.getHeader("Source");
                if (source == null || source.isBlank()) {
                    source = "Empty";
                }

                // Get header "TraceId" (case-insensitive)
                String traceId = request.getHeader("TraceId");
                if (traceId == null || traceId.isBlank()) {
                    traceId = "Empty";
                }

                var sourceScope = tracer.createBaggageInScope("source", source);
                var traceIdScope = tracer.createBaggageInScope("traceid", traceId);

                try {
                    filterChain.doFilter(request, response);
                } finally {
                    sourceScope.close();
                    traceIdScope.close();
                }
            }
        };
    }
}
