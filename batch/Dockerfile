FROM openjdk:21-slim

ARG JAR_FILE

WORKDIR /workspace

# install curl for health checks
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

COPY ${JAR_FILE} /workspace/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]