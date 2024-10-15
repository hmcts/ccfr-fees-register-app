package uk.gov.hmcts.fees.register.api.configuration;

import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfiguration {
    private static final String HEADER = "header";

    @Bean
    public GroupedOpenApi oldFeesRegister() {

        return GroupedOpenApi.builder()
            .group("fees-register")
            .packagesToScan("uk.gov.hmcts.fees.register.api.controllers")
            .pathsToMatch("/**")
            .addOperationCustomizer(authorizationHeaders())
            .build();
    }

    @Bean
    public GroupedOpenApi newFeesRegister() {

        return GroupedOpenApi.builder()
            .group("fees2-register")
            .packagesToScan("uk.gov.hmcts.fees2.register.api.controllers")
            .pathsToMatch("/**")
            .addOperationCustomizer(authorizationHeaders())
            .build();
    }

    @Bean
    public OperationCustomizer authorizationHeaders() {
        return (operation, handlerMethod) ->
            operation
                .addParametersItem(
                    mandatoryStringParameter("Authorization", "User authorization header"))
                .addParametersItem(
                    mandatoryStringParameter("ServiceAuthorization", "Service authorization header"));
    }

    private Parameter mandatoryStringParameter(String name, String description) {
        return new Parameter()
            .name(name)
            .description(description)
            .required(true)
            .in(HEADER)
            .schema(new StringSchema());
    }
}
