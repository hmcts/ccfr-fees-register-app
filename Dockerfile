# renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.6.2
FROM hmctspublic.azurecr.io/base/java:21-distroless

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/fees-register-app.jar /opt/app/

EXPOSE 8080

CMD [ \
    "--add-opens", "java.base/java.lang=ALL-UNNAMED", \
    "fees-register-app.jar" \
    ]
