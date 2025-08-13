# Use a minimal OpenJDK 17 base image
# FROM eclipse-temurin:17-jdk-alpine as base
FROM maven:3.8.5-openjdk-17-slim AS base

# Set working directory
WORKDIR /app

# Add a non-root user for security (optional but recommended)

#RUN adduser -D -u 1000 appuser
COPY dependencies .

# Go to the extracted 'dependencies' folder and execute below commands to manually install the maven dependency.

# 1)Installation of Uno Java Foundation BOM
RUN mvn install:install-file -Dfile=uno-java-foundation-bom-1.0.0-SNAPSHOT.pom -DgroupId=com.ambry -DartifactId=uno-java-foundation-bom -Dversion=1.0.0-SNAPSHOT -Dpackaging=pom

# 2)Installation of Uno Spring boot Foundation BOM
RUN mvn install:install-file -Dfile=uno-spring-boot-foundation-bom-1.0.0-SNAPSHOT.pom -DgroupId=com.ambry -DartifactId=uno-spring-boot-foundation-bom -Dversion=1.0.0-SNAPSHOT -Dpackaging=pom

# 3)Installation of Uno Spring Boot Framework
RUN mvn install:install-file -Dfile=uno-spring-boot-framework-0.0.1-SNAPSHOT.jar -DgroupId=com.ambry -DartifactId=uno-spring-boot-framework -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar

#RUN mvn install:install-file -Dfile=RiskObjects.jar -DgroupId=com.objex -DartifactId=RiskObjects -Dversion=1.3 -Dpackaging=jar

RUN mvn install:install-file -Dfile=GailModel-0.0.1-SNAPSHOT.jar -DgroupId=com.progeny -DartifactId=GailModel -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar

# 4 bayes tries

RUN mvn install:install-file -Dfile=REngine.jar -DgroupId=org.rosouda -DartifactId=REngine -Dversion=1.8.0_241 -Dpackaging=jar
RUN mvn install:install-file -Dfile=Rserve.jar -DgroupId=org.rosouda -DartifactId=Rserve -Dversion=1.8.0_241 -Dpackaging=jar

RUN mvn install:install-file -Dfile=RiskObjects-1.4.jar -DgroupId=com.objex -DartifactId=RiskObjects -Dversion=1.4 -Dpackaging=jar

COPY src .
COPY pom.xml .
COPY sonar-project.properties .
# Creates JAR file in target directory
RUN mvn package 

# Copy the JAR file
COPY target/uno-risk-service.jar app.jar

#Expose the port
EXPOSE 8080

# Set ownership to the app user
#RUN chown -R appuser:appuser /app

# Switch to non-root user
#USER appuser


# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
