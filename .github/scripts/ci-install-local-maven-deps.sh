#!/usr/bin/env bash
# Install vendored BOMs/JARs into ~/.m2 (required before Maven/Snyk can resolve this project).
set -euo pipefail

# Snyk runs ./mvnw when present; ensure executable on Linux CI (Windows checkouts may store 100644).
if [ -f mvnw ]; then
  chmod +x mvnw
fi

mvn install:install-file -Dfile=./dependencies/uno-java-foundation-bom-1.0.1-SNAPSHOT.pom -DgroupId=com.ambry -DartifactId=uno-java-foundation-bom -Dversion=1.0.1-SNAPSHOT -Dpackaging=pom
mvn install:install-file -Dfile=./dependencies/uno-spring-boot-foundation-bom-1.0.1-SNAPSHOT.pom -DgroupId=com.ambry -DartifactId=uno-spring-boot-foundation-bom -Dversion=1.0.1-SNAPSHOT -Dpackaging=pom
mvn install:install-file -Dfile=./dependencies/uno-spring-boot-framework-0.0.2-SNAPSHOT.jar -DgroupId=com.ambry -DartifactId=uno-spring-boot-framework -Dversion=0.0.2-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=./dependencies/GailModel-0.0.1-SNAPSHOT.jar -DgroupId=com.progeny -DartifactId=GailModel -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=./dependencies/REngine.jar -DgroupId=org.rosuda.REngine -DartifactId=REngine -Dversion=2.1.0 -Dpackaging=jar
mvn install:install-file -Dfile=./dependencies/Rserve.jar -DgroupId=org.rosuda.REngine -DartifactId=Rserve -Dversion=1.8.1 -Dpackaging=jar
mvn install:install-file -Dfile=./dependencies/RiskObjects-2.1.jar -DgroupId=com.objex -DartifactId=RiskObjects -Dversion=2.1 -Dpackaging=jar
