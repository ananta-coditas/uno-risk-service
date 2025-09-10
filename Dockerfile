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
RUN mvn install:install-file -Dfile=./dependencies/uno-java-foundation-bom-1.0.0-SNAPSHOT.pom -DgroupId=com.ambry -DartifactId=uno-java-foundation-bom -Dversion=1.0.0-SNAPSHOT -Dpackaging=pom

# 2)Installation of Uno Spring boot Foundation BOM
RUN mvn install:install-file -Dfile=./dependencies/uno-spring-boot-foundation-bom-1.0.0-SNAPSHOT.pom -DgroupId=com.ambry -DartifactId=uno-spring-boot-foundation-bom -Dversion=1.0.0-SNAPSHOT -Dpackaging=pom

# 3)Installation of Uno Spring Boot Framework
RUN mvn install:install-file -Dfile=./dependencies/uno-spring-boot-framework-0.0.1-SNAPSHOT.jar -DgroupId=com.ambry -DartifactId=uno-spring-boot-framework -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar

#RUN mvn install:install-file -Dfile=RiskObjects.jar -DgroupId=com.objex -DartifactId=RiskObjects -Dversion=1.3 -Dpackaging=jar

RUN mvn install:install-file -Dfile=./dependencies/GailModel-0.0.1-SNAPSHOT.jar -DgroupId=com.progeny -DartifactId=GailModel -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar

# 4 bayes tries

RUN mvn install:install-file -Dfile=./dependencies/REngine.jar -DgroupId=org.rosouda -DartifactId=REngine -Dversion=1.8.0_241 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./dependencies/Rserve.jar -DgroupId=org.rosouda -DartifactId=Rserve -Dversion=1.8.0_241 -Dpackaging=jar

RUN mvn install:install-file -Dfile=./dependencies/RiskObjects-1.4.jar -DgroupId=com.objex -DartifactId=RiskObjects -Dversion=1.4 -Dpackaging=jar

COPY pom.xml .
COPY src/ src/
COPY sonar-project.properties .
# Creates JAR file in target directory
RUN mvn clean package -DskipTests

# Copy the JAR file

#RUN cp target/*.jar uno-risk-service.jar


### Stage 2 ###

FROM rockylinux/rockylinux:9.5

#for R
RUN dnf install -y epel-release && dnf config-manager --set-enabled crb && dnf update -y
RUN dnf update -y && dnf install -y glibc libgcc java-21-openjdk R-core R-core-devel  && dnf clean all    #gcc gcc-c++ gcc-fortran make

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

CMD bash -c "R -e \"Rserve::Rserve(args='--RS-enable-remote --no-save')\" & java -jar /app/uno-risk-service.jar"
