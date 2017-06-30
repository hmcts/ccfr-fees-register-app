package uk.gov.hmcts.fees.register.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uk.gov.hmcts.fees.register.api.controllers.FeesRegisterController;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("common-components").apiInfo(apiInfo()).select()
            .apis(RequestHandlerSelectors.basePackage(FeesRegisterController.class.getPackage().getName())).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Fee Register API")
            .description("Fee Register API to retrieve the correct fee.")

            .contact(new Contact("Sachi Kuppuswami, Kazys Sketrys, Jalal ul Deen ", "", "jalal.deen@hmcts.net"))
            .version("1.0")
            .build();
    }
}
