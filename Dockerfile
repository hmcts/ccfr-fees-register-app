FROM openjdk:8-jre

COPY docker/entrypoint.sh /

EXPOSE 8080

COPY api/target/fees-register-api-*.jar /app.jar

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy= curl --silent --fail http://localhost:8080/health

ENTRYPOINT [ "/entrypoint.sh" ]
