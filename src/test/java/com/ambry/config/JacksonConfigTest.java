package com.ambry.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * Unit tests for {@link JacksonConfig}.
 */
@SpringBootTest(classes = { JacksonConfig.class })
class JacksonConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testJavaTimeModuleBean() {
        JavaTimeModule javaTimeModule = applicationContext.getBean(JavaTimeModule.class);
        assertThat(javaTimeModule).isNotNull();
    }

    @Test
    void testJdk8TimeModuleBean() {
        Jdk8Module jdk8Module = applicationContext.getBean(Jdk8Module.class);
        assertThat(jdk8Module).isNotNull();
    }

    @Test
    void testCustomSerializerBean() {
        // There are multiple SimpleModule beans, so we need to get by name
        SimpleModule customSerializer = applicationContext.getBean("customSerializer", SimpleModule.class);
        assertThat(customSerializer).isNotNull();
    }

    @Test
    void testObjectMapperBean() {
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        assertThat(objectMapper).isNotNull();
    }

    @Test
    void testObjectMapperConfiguration() {
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        assertThat(objectMapper.getDeserializationConfig().isEnabled(
            com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
        )).isFalse();
    }
}

