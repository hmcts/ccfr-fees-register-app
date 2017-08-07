package uk.gov.hmcts.fees.register.api;

import org.springframework.boot.builder.SpringApplicationBuilder;


public class EmbeddedFeesRegisterServiceApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .sources(FeesRegisterServiceApplication.class)
            .profiles("embedded", "idam-test")
            .run();
    }
}
