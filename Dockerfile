FROM hmcts/cnp-java-base:openjdk-jre-8-alpine-1.4

# Mandatory!
ENV APP fees-register-app.jar
ENV APPLICATION_TOTAL_MEMORY 1024M
ENV APPLICATION_SIZE_ON_DISK_IN_MB 365

# Optional
ENV JAVA_OPTS ""

COPY build/libs/$APP /opt/app/

EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy= curl --silent --fail http://localhost:8080/health
