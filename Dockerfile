ARG APP_INSIGHTS_AGENT_VERSION=2.5.1
FROM hmctspublic.azurecr.io/base/java:17-distroless

EXPOSE 8080

COPY build/libs/fees-register-app.jar /opt/app/
COPY lib/AI-Agent.xml /opt/app/

CMD ["fees-register-app.jar"]
