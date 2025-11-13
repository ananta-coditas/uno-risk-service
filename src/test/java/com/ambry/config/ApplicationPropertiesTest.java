package com.ambry.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ApplicationProperties}.
 */
class ApplicationPropertiesTest {

    private ApplicationProperties applicationProperties;

    @BeforeEach
    void setUp() {
        applicationProperties = new ApplicationProperties();
    }

    @Test
    void testTyrerConfigGetterAndSetter() {
        ApplicationProperties.TyrerConfig tyrerConfig = applicationProperties.new TyrerConfig();
        applicationProperties.setTyrer(tyrerConfig);

        assertThat(applicationProperties.getTyrer()).isEqualTo(tyrerConfig);
    }

    @Test
    void testBayesMendelConfigGetterAndSetter() {
        ApplicationProperties.BayesMendelConfig bayesMendelConfig = applicationProperties.new BayesMendelConfig();
        applicationProperties.setBayesMendel(bayesMendelConfig);

        assertThat(applicationProperties.getBayesMendel()).isEqualTo(bayesMendelConfig);
    }

    @Test
    void testTyrerConfigDefaultInstance() {
        assertThat(applicationProperties.getTyrer()).isNotNull();
    }

    @Test
    void testBayesMendelConfigDefaultInstance() {
        assertThat(applicationProperties.getBayesMendel()).isNotNull();
    }

    @Test
    void testTyrerConfigPlatformLinux() {
        ApplicationProperties.TyrerConfig tyrerConfig = applicationProperties.getTyrer();
        ApplicationProperties.TyrerConfig.Platform linux = new ApplicationProperties.TyrerConfig.Platform();
        linux.setCommand("linux-command");
        linux.setSavePath("/linux/path");

        tyrerConfig.setLinux(linux);

        assertThat(tyrerConfig.getLinux()).isEqualTo(linux);
        assertThat(tyrerConfig.getLinux().getCommand()).isEqualTo("linux-command");
        assertThat(tyrerConfig.getLinux().getSavePath()).isEqualTo("/linux/path");
    }

    @Test
    void testTyrerConfigPlatformWindows() {
        ApplicationProperties.TyrerConfig tyrerConfig = applicationProperties.getTyrer();
        ApplicationProperties.TyrerConfig.Platform windows = new ApplicationProperties.TyrerConfig.Platform();
        windows.setCommand("windows-command");
        windows.setSavePath("C:\\windows\\path");

        tyrerConfig.setWindows(windows);

        assertThat(tyrerConfig.getWindows()).isEqualTo(windows);
        assertThat(tyrerConfig.getWindows().getCommand()).isEqualTo("windows-command");
        assertThat(tyrerConfig.getWindows().getSavePath()).isEqualTo("C:\\windows\\path");
    }

    @Test
    void testBayesMendelConfigPlatformLinux() {
        ApplicationProperties.BayesMendelConfig bayesMendelConfig = applicationProperties.getBayesMendel();
        ApplicationProperties.BayesMendelConfig.Platform linux = new ApplicationProperties.BayesMendelConfig.Platform();
        linux.setCommand("linux-command");
        linux.setSavePath("/linux/path");
        linux.setSpawn("spawn-command");

        bayesMendelConfig.setLinux(linux);

        assertThat(bayesMendelConfig.getLinux()).isEqualTo(linux);
        assertThat(bayesMendelConfig.getLinux().getCommand()).isEqualTo("linux-command");
        assertThat(bayesMendelConfig.getLinux().getSavePath()).isEqualTo("/linux/path");
        assertThat(bayesMendelConfig.getLinux().getSpawn()).isEqualTo("spawn-command");
    }

    @Test
    void testBayesMendelConfigPlatformWindows() {
        ApplicationProperties.BayesMendelConfig bayesMendelConfig = applicationProperties.getBayesMendel();
        ApplicationProperties.BayesMendelConfig.Platform windows = new ApplicationProperties.BayesMendelConfig.Platform();
        windows.setCommand("windows-command");
        windows.setSavePath("C:\\windows\\path");
        windows.setSpawn("spawn-command");

        bayesMendelConfig.setWindows(windows);

        assertThat(bayesMendelConfig.getWindows()).isEqualTo(windows);
        assertThat(bayesMendelConfig.getWindows().getCommand()).isEqualTo("windows-command");
        assertThat(bayesMendelConfig.getWindows().getSavePath()).isEqualTo("C:\\windows\\path");
        assertThat(bayesMendelConfig.getWindows().getSpawn()).isEqualTo("spawn-command");
    }

    @Test
    void testBayesMendelConfigProperties() {
        ApplicationProperties.BayesMendelConfig bayesMendelConfig = applicationProperties.getBayesMendel();
        bayesMendelConfig.setMaxConnections("10");
        bayesMendelConfig.setSaveAll("true");
        bayesMendelConfig.setPrint("false");

        assertThat(bayesMendelConfig.getMaxConnections()).isEqualTo("10");
        assertThat(bayesMendelConfig.getSaveAll()).isEqualTo("true");
        assertThat(bayesMendelConfig.getPrint()).isEqualTo("false");
    }
}

