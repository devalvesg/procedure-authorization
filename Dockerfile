# Stage 1 - build with Maven
FROM maven:3.9.6-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# Stage 2 - WildFly runtime with WAR
FROM quay.io/wildfly/wildfly:latest
ENV APP_NAME procedure_authorization
COPY --from=build /app/target/${APP_NAME}.war /opt/jboss/wildfly/standalone/deployments/ROOT.war
EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"] 
