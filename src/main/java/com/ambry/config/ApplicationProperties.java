package com.ambry.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Application.
 * <p>
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private TyrerConfig tyrer = new TyrerConfig();
    private BayesMendelConfig bayesMendel = new BayesMendelConfig();

    private String publicPath;

    public TyrerConfig getTyrer() {
        return tyrer;
    }
    public BayesMendelConfig getBayesMendel() {
        return bayesMendel;
    }
    public void setBayesMendel(BayesMendelConfig bayesMendel) {
        this.bayesMendel = bayesMendel;
    }

    public void setTyrer(TyrerConfig tyrer) {
        this.tyrer = tyrer;
    }
    public String getPublicPath() {
        return publicPath;
    }

    public void setPublicPath(String publicPath) {
            if (publicPath == null || publicPath.isBlank()) {
                this.publicPath = null;
            } else {
                this.publicPath = publicPath;
            }
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

    public class BayesMendelConfig {

        private Platform linux;
        private Platform windows;
        private String maxConnections;
        private String saveAll;
        private String print;

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

        public String getSaveAll() {
			return saveAll;
		}

		public void setSaveAll(String saveAll) {
			this.saveAll = saveAll;
		}

		public String getMaxConnections() {
			return maxConnections;
		}

		public void setMaxConnections(String maxConnections) {
			this.maxConnections = maxConnections;
		}

		public String getPrint() {
			return print;
		}

		public void setPrint(String print) {
			this.print = print;
		}

		public static class Platform {
            private String command;
            private String savePath;
            private String spawn;

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
            public String getSpawn() {
                return spawn;
            }

            public void setSpawn(String spawn) {
                this.spawn = spawn;
            }
        }
    }
}
