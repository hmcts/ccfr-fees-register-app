ARG APP_INSIGHTS_AGENT_VERSION=2.5.0
FROM hmctspublic.azurecr.io/base/java:openjdk-8-distroless-1.0

EXPOSE 8080

COPY build/libs/fees-register-app.jar /opt/app/
COPY lib/applicationinsights-agent-2.5.0.jar lib/AI-Agent.xml /opt/app/
COPY --chown=root ./fees-register-audit.log /opt/app

CMD ["fees-register-app.jar"]
