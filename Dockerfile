# Patched JRE for runtime (aligned with jib-maven-plugin.image); avoids Rocky java-21-openjdk-headless RPM scan surface
FROM eclipse-temurin:21-jre-jammy AS jre
# CVE-2026-16742: libsystemd0 >= 249.11-0ubuntu3.22 (systemd-homed home-record signature verification)
RUN apt-get update \
  && apt-get install -y --no-install-recommends libsystemd0 \
  && rm -rf /var/lib/apt/lists/*

FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Add a non-root user for security (optional but recommended)

#RUN adduser -D -u 1000 appuser
COPY dependencies/ dependencies/

# Go to the extracted 'dependencies' folder and execute below commands to manually install the maven dependency.

# 1)Installation of Uno Java Foundation BOM
RUN mvn install:install-file -Dfile=./dependencies/uno-java-foundation-bom-1.0.1-SNAPSHOT.pom -DgroupId=com.ambry -DartifactId=uno-java-foundation-bom -Dversion=1.0.1-SNAPSHOT -Dpackaging=pom

# 2)Installation of Uno Spring boot Foundation BOM
RUN mvn install:install-file -Dfile=./dependencies/uno-spring-boot-foundation-bom-1.0.1-SNAPSHOT.pom -DgroupId=com.ambry -DartifactId=uno-spring-boot-foundation-bom -Dversion=1.0.1-SNAPSHOT -Dpackaging=pom

# 3)Installation of Uno Spring Boot Framework
RUN mvn install:install-file -Dfile=./dependencies/uno-spring-boot-framework-0.0.2-SNAPSHOT.jar -DgroupId=com.ambry -DartifactId=uno-spring-boot-framework -Dversion=0.0.2-SNAPSHOT -Dpackaging=jar

#RUN mvn install:install-file -Dfile=RiskObjects.jar -DgroupId=com.objex -DartifactId=RiskObjects -Dversion=1.3 -Dpackaging=jar

RUN mvn install:install-file -Dfile=./dependencies/GailModel-0.0.1-SNAPSHOT.jar -DgroupId=com.progeny -DartifactId=GailModel -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar

# 4 bayes tries

RUN mvn install:install-file -Dfile=./dependencies/REngine.jar -DgroupId=org.rosouda -DartifactId=REngine -Dversion=1.8.0_241 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./dependencies/Rserve.jar -DgroupId=org.rosouda -DartifactId=Rserve -Dversion=1.8.0_241 -Dpackaging=jar

RUN mvn install:install-file -Dfile=./dependencies/RiskObjects-2.1.jar -DgroupId=com.objex -DartifactId=RiskObjects -Dversion=2.1 -Dpackaging=jar

COPY pom.xml .
COPY src/ src/
COPY sonar-project.properties .
# Creates JAR file in target directory
RUN mvn clean package -DskipTests

# Datadog Java tracer (fetched in build stage so Rocky runtime image need not invoke curl)
RUN curl -fsSL https://dtdg.co/latest-java-tracer -o /app/dd-java-agent.jar

# Copy the JAR file

#RUN cp target/*.jar uno-risk-service.jar


### Stage 2 ###

FROM rockylinux/rockylinux:9.5

#for R
# CVE-2026-86140 / CVE-2026-74860: libxml2 — dnf update pulls latest Rocky 9 RPM (2.9.13-14.el9_8.4); no el9 backport yet for these CVEs (.grype.yaml accepted-risk)
# CVE-2026-86145 / CVE-2026-89161: pcre2 — fixed upstream PCRE2 10.48; Rocky 9 <10.48 via dnf update; no el9 backport yet (.grype.yaml accepted-risk)
# CVE-2026-66046: expat — fixed upstream libexpat 2.8.4; Rocky 9 expat 2.5.0-6.el9_8.3 via dnf update; no el9 backport yet (.grype.yaml accepted-risk)
RUN dnf install -y epel-release && dnf config-manager --set-enabled crb && dnf update -y
RUN dnf install -y \
      glibc \
      libgcc \
      libxml2 \
      pcre2 \
      expat \
      R-core \
      R-core-devel \
  && dnf remove -y vim-minimal \
  && dnf clean all \
  && rm -rf /var/cache/dnf /tmp/* /var/tmp/* /usr/share/doc /usr/share/man /usr/share/locale

# Eclipse Temurin 21 JRE (not Rocky java-21-openjdk-headless); rebuild picks up 21.0.12+ when published (CVE-2026-47063)
COPY --from=jre /opt/java/openjdk /opt/java/openjdk
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"
# && dnf remove -y R-core-devel \
#gcc gcc-c++ gcc-fortran make

# Use a minimal OpenJDK 17 base image
# FROM eclipse-temurin:17-jdk-alpine as base

# Set working directory
WORKDIR /app

# Add a non-root user for security (optional but recommended)
#for alpine RUN adduser -D -u 1000 appuser
#RUN useradd -u 1000 -M --no-create-home --shell /sbin/nologin appuser
RUN useradd -u 1000 --shell /sbin/nologin appuser

# Copy the JAR file
COPY --from=build /app/target/uno-risk-service.jar uno-risk-service.jar

# Copy Tyrer-Cuzick
COPY src/main/resources/risk_model_dependency/tcuzick /opt/tyrer-server/tcuzick
RUN chmod 755 /opt/tyrer-server/tcuzick
RUN chmod +x /opt/tyrer-server/tcuzick
RUN chown -R appuser:appuser /opt/tyrer-server

# Create a user-specific R library directory
RUN mkdir -p /app/R_libs && \
    chmod -R 775 /app

# Set R library path to a writable directory
ENV R_LIBS_USER=/app/R_libs


#Copy Bayes Dependencies
COPY src/main/resources/risk_model_dependency/BCRA_2.1.2.tar.gz /tmp
COPY src/main/resources/risk_model_dependency/BayesMendel_2.1-8.tar.gz /tmp

#installs bayes mendel dependencies
RUN R -e "install.packages('/tmp/BCRA_2.1.2.tar.gz',type='source',REPOS= NULL, dependencies=TRUE)"
RUN R -e "install.packages('kinship2', dependencies=TRUE, repos = 'https://cloud.r-project.org')"
RUN R -e "install.packages('Rserve', dependencies=TRUE, repos = 'https://cloud.r-project.org')"
RUN R -e "install.packages('/tmp/BayesMendel_2.1-8.tar.gz',type='source',REPOS= NULL, dependencies=TRUE)"

COPY --from=build /app/dd-java-agent.jar /app/dd-java-agent.jar

# Set ownership to the app user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

COPY src/main/resources/risk_model_data/nccn_guide_lines_details.json /home/appuser/risk_model_data/nccn_guide_lines_details.json

#Expose the port
EXPOSE 8080

# Set ownership to the app user
#RUN chown -R appuser:appuser /app

# Switch to non-root user
#USER appuser


# Run the Spring Boot app
# ENTRYPOINT ["java", "-jar", "uno-risk-service.jar"]

CMD bash -c "R -e \"Rserve::Rserve(args='--RS-enable-remote --no-save')\" & java -javaagent:/app/dd-java-agent.jar -Ddd.service=uno-risk-service -Ddd.env=\${DD_ENV:-prod} -Ddd.version=\${DD_VERSION:-unknown} -Ddd.trace.otel.enabled=true -Ddd.trace.agent.url=${DD_TRACE_AGENT_URL:localhost} -Ddd.trace.agent.port=8126 -jar /app/uno-risk-service.jar"
