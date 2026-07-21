# ---------------------------------------------
# BUILD STAGE
# ---------------------------------------------

FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# ---------------------------------------------
# RUNTIME STAGE
# ---------------------------------------------

FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy jar file
COPY --from=build /app/target/*.jar app.jar

# Application Port
EXPOSE 8080

# JVM Optimizations
ENTRYPOINT ["java", \
"-XX:+UseContainerSupport", \
"-XX:MaxRAMPercentage=75.0", \
"-jar", \
"app.jar"]