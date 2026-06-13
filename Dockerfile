FROM gradle:9.5.1-jdk21 AS builder

COPY . /app
WORKDIR /app
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21.0.11_10-jre

WORKDIR /var/appl

COPY --from=builder /app/build/container .

HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.edg=file:/dev/./urandrom -jar spring-temporal.jar", "-d"]