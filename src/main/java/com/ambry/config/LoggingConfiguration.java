package com.ambry.config;

import static com.ambry.config.logging.LoggingUtils.*;

import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/*
 * Configures the console and Logstash log appenders from the app properties
 */
@Configuration
public class LoggingConfiguration {

    public LoggingConfiguration(
        @Value("${spring.application.name}") String appName,
        @Value("${server.port}") String serverPort,
        UnoFrameworkConfig unoFrameworkConfigProperties,
        ObjectMapper mapper
    ) throws JsonProcessingException {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        Map<String, String> map = new HashMap<>();
        map.put("app_name", appName);
        map.put("app_port", serverPort);
        String customFields = mapper.writeValueAsString(map);

        UnoFrameworkConfig.Logging loggingProperties = unoFrameworkConfigProperties.getLogging();
        UnoFrameworkConfig.Logging.Logstash logstashProperties = loggingProperties.getLogstash();

        if (loggingProperties.isUseJsonFormat()) {
            addJsonConsoleAppender(context, customFields);
        }
        if (logstashProperties.isEnabled()) {
            addLogstashTcpSocketAppender(context, customFields, logstashProperties);
        }
        if (loggingProperties.isUseJsonFormat() || logstashProperties.isEnabled()) {
            addContextListener(context, customFields, loggingProperties);
        }
    }
}
