package uk.gov.hmcts.fees.register.functional;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-functional-tests.properties")
public class TestContextConfiguration {
}
