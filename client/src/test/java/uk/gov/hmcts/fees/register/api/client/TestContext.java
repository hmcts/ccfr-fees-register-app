package uk.gov.hmcts.fees.register.api.client;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@ComponentScan(basePackages = {"uk.gov.hmcts.fees.register"})
@ContextConfiguration
public class TestContext {


}
