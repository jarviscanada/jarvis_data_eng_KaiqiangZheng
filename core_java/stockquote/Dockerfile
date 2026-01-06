# 1. Use base image (Java 17 Alpine version, smaller in size) openjdk:17-alpine expired
FROM eclipse-temurin:8-jdk-alpine

# 2. Create working directory
WORKDIR /app

# 3. Copy the packaged JAR file into the image
COPY target/stock_quote_app-1.0-SNAPSHOT.jar /app/app.jar

# 4. Create a logs directory (since our logback configuration outputs to logs/)
RUN mkdir -p /app/logs

# 5. Define the startup command
# When the Docker container starts, run java -jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]