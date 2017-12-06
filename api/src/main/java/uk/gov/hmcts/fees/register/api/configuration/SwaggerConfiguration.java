package uk.gov.hmcts.fees.register.api.configuration;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Collections.singletonList;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket oldFeesRegister() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("fees-register")
            .globalOperationParameters(singletonList(
                new ParameterBuilder()
                    .name("Authorization")
                    .description("User authorization header")
                    .required(false)
                    .parameterType("header")
                    .modelRef(new ModelRef("string"))
                    .build()
            ))
            .apiInfo(oldFeesApiInfo()).select()
            .apis(packagesLike("uk.gov.hmcts.fees.register.api.controllers"))
            .paths(PathSelectors.any())
            .build();
    }

    @Bean
    public Docket newFeesRegister() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("fees2-register")
            .globalOperationParameters(singletonList(
                new ParameterBuilder()
                    .name("Authorization")
                    .description("User authorization header")
                    .required(false)
                    .parameterType("header")
                    .modelRef(new ModelRef("string"))
                    .build()
            ))
            .useDefaultResponseMessages(false)
            .apiInfo(newFeesApiInfo()).select()
            .apis(packagesLike("uk.gov.hmcts.fees2.register.api.controllers"))
            .paths(PathSelectors.any())
            .build();
    }

    private static Predicate<RequestHandler> packagesLike(final String pkg) {
        return input -> input.declaringClass().getPackage().getName().startsWith(pkg);
    }

    private ApiInfo oldFeesApiInfo() {
        return new ApiInfoBuilder()
            .title("FeeOld Register API")
            .description("FeeOld Register API to retrieve the correct fee.")
            .contact(new Contact("Sachi Kuppuswami, Kazys Sketrys, Jalal ul Deen ", "", "jalal.deen@hmcts.net"))
            .version("1.0")
            .build();
    }

    private ApiInfo newFeesApiInfo() {
        return new ApiInfoBuilder()
            .title("New Fee API")
            .description("New Fee API to retrieve the correct fee.")
            .contact(new Contact("Eduardo Magdalena, Tarun Palisetty", "", "eduardo.magdalena@hmcts.net, tarun.palisetty@hmcts.net"))
            .version("1.0")
            .build();
    }
}
