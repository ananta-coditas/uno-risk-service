FROM rockylinux/rockylinux:9.5
#for R
RUN dnf install -y epel-release && dnf config-manager --set-enabled crb && dnf update -y

RUN dnf update -y && dnf install -y glibc libgcc java-17-openjdk R-core R-core-devel gcc gcc-c++ gcc-fortran make  && dnf clean all

# Use a minimal OpenJDK 17 base image
# FROM eclipse-temurin:17-jdk-alpine as base

# Set working directory
WORKDIR /app

# Add a non-root user for security (optional but recommended)
#for alpine RUN adduser -D -u 1000 appuser
#RUN useradd -u 1000 -M --no-create-home --shell /sbin/nologin appuser
RUN useradd -u 1000 --shell /sbin/nologin appuser

# Copy the JAR file
COPY target/uno-risk-service.jar app.jar

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

#Expose the port
EXPOSE 8080


# Set ownership to the app user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser


# Run the Spring Boot app
# ENTRYPOINT ["java", "-jar", "app.jar"]
CMD bash -c "R -e \"Rserve::Rserve(args='--RS-enable-remote --no-save')\" & java -jar /app/app.jar"