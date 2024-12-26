package com.ambry.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Application.
 * <p>
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private TyrerConfig tyrer = new TyrerConfig();
    
    public TyrerConfig getTyrer() {
        return tyrer;
    }

    public void setTyrer(TyrerConfig tyrer) {
        this.tyrer = tyrer;
    }

    public class TyrerConfig {

        private Platform linux;
        private Platform windows;

        public Platform getLinux() {
            return linux;
        }

        public void setLinux(Platform linux) {
            this.linux = linux;
        }

        public Platform getWindows() {
            return windows;
        }

        public void setWindows(Platform windows) {
            this.windows = windows;
        }

        public static class Platform {
            private String command;
            private String savePath;

            public String getCommand() {
                return command;
            }

            public void setCommand(String command) {
                this.command = command;
            }

            public String getSavePath() {
                return savePath;
            }

            public void setSavePath(String savePath) {
                this.savePath = savePath;
            }
        }
    }
}
