# Use a minimal OpenJDK 21 base image
# FROM eclipse-temurin:21-jdk-alpine as base
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

# Copy the JAR file

#RUN cp target/*.jar uno-risk-service.jar


### Stage 2 ###
# Rocky Linux 9.6 + OpenJDK 21.0.10+ with patched libpng (>=1.6.55) to fix:
# - CVE-2025-66293 (libpng oob read): https://nvd.nist.gov/vuln/detail/CVE-2025-66293
# - CVE-2026-25646 (libpng heap overflow in png_set_quantize): https://nvd.nist.gov/vuln/detail/CVE-2026-25646
FROM rockylinux/rockylinux:9.6

# Single RUN: update repos then install so we always get latest patched java-21-openjdk-headless (no stale cache).
# dnf update also pulls patched system libs when available (e.g. libarchive for CVE-2026-4111).
RUN dnf install -y epel-release && dnf config-manager --set-enabled crb \
  && dnf update -y \
  && dnf install -y \
      glibc \
      libgcc \
      libpng \
      R-core \
      R-core-devel \
      'java-21-openjdk-headless >= 21.0.10.0.7' \
  && dnf clean all \
  && rm -rf /var/cache/dnf /tmp/* /var/tmp/* /usr/share/doc /usr/share/man /usr/share/locale
# Fail build if OpenJDK is not 21.0.10+ (required for CVE-2025-66293 and CVE-2026-25646)
RUN java -version 2>&1 | grep -qE "21\.0\.(1[0-9]|[2-9][0-9])" || (echo "ERROR: OpenJDK 21.0.10+ required for CVE-2025-66293 and CVE-2026-25646" && exit 1)
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

RUN curl -L https://dtdg.co/latest-java-tracer -o /app/dd-java-agent.jar

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
