package uk.gov.hmcts.fees.register.api.client;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComponentFactoryConfiguration {

    @Bean
    public RestTemplateBuilder getTemplateBuilder() {
        return new RestTemplateBuilder();
    }
}
