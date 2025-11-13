package com.ambry.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.FormattingConversionService;

/**
 * Unit tests for {@link DateTimeFormatConfiguration}.
 */
class DateTimeFormatConfigurationTest {

    @Test
    void testDateTimeFormatConfigurationExists() {
        DateTimeFormatConfiguration config = new DateTimeFormatConfiguration();
        assertThat(config).isNotNull();
    }

    @Test
    void testLocalDateFormatter() {
        DateTimeFormatConfiguration config = new DateTimeFormatConfiguration();
        FormattingConversionService service = new FormattingConversionService();
        config.addFormatters(service);

        assertThat(service.canConvert(String.class, LocalDate.class)).isTrue();
    }

    @Test
    void testLocalDateTimeFormatter() {
        DateTimeFormatConfiguration config = new DateTimeFormatConfiguration();
        FormattingConversionService service = new FormattingConversionService();
        config.addFormatters(service);

        assertThat(service.canConvert(String.class, LocalDateTime.class)).isTrue();
    }

    @Test
    void testIsoFormatUsage() {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        FormattingConversionService service = new FormattingConversionService();
        registrar.registerFormatters(service);

        LocalDate date = LocalDate.parse("2024-01-15", DateTimeFormatter.ISO_LOCAL_DATE);
        assertThat(date).isNotNull();
        assertThat(date.getYear()).isEqualTo(2024);
        assertThat(date.getMonthValue()).isEqualTo(1);
        assertThat(date.getDayOfMonth()).isEqualTo(15);
    }
}

