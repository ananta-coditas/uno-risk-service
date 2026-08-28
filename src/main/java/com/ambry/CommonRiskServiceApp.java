package com.ambry;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ambry.config.ApplicationProperties;
import com.ambry.config.CRLFLogConverter;
import com.ambry.config.DefaultProfileUtil;
import com.ambry.config.UnoFrameworkConfig;
import com.ambry.config.UnoFrameworkConstants;

import jakarta.annotation.PostConstruct;
;

@SpringBootApplication(excludeName = {
    "org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration"
})
@EnableConfigurationProperties({UnoFrameworkConfig.class, ApplicationProperties.class })
@ComponentScan(basePackages = {"com.ambry", "com.objex.progeny.risk.services","com.objex.risk.services", "com.genomebrain.backend"})
public class CommonRiskServiceApp { 

    private static final Logger LOG = LoggerFactory.getLogger(CommonRiskServiceApp.class);

    private  Environment env;

    public CommonRiskServiceApp() {
    	
    }
    public CommonRiskServiceApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes Application.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     */
    @PostConstruct
    public void initApplication() {
    	LOG.info("Environment in initApplication " + env);
        Collection<String> activeProfiles = env == null ? new ArrayList() : Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(UnoFrameworkConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            LOG.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
            );
        }
        if (
            activeProfiles.contains(UnoFrameworkConstants.SPRING_PROFILE_DEVELOPMENT) ) {
            LOG.error(
                "You have misconfigured your application! It should not " + "run with both the 'dev' and 'cloud' profiles at the same time."
            );
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CommonRiskServiceApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        System.out.println(" arguments:" + args.length);
        for (int i = 0; i < args.length; i++) {
        	System.out.println(i + " arguments:" +  args[i]);
        }
        
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }
    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String applicationName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOG.warn("The host name could not be determined, using `localhost` as fallback");
        }
        LOG.info(
            CRLFLogConverter.CRLF_SAFE_MARKER,
            """

            ----------------------------------------------------------
            \tApplication '{}' is running! Access URLs:
            \tLocal: \t\t{}://localhost:{}{}
            \tExternal: \t{}://{}:{}{}
            \tProfile(s): \t{}
            ----------------------------------------------------------""",
            applicationName,
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }
}
