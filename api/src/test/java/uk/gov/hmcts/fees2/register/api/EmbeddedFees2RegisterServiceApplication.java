package uk.gov.hmcts.fees2.register.api;

import org.springframework.boot.builder.SpringApplicationBuilder;
import uk.gov.hmcts.FeesRegisterServiceApplication;

/**
 * Created by tarun on 02/11/2017.
 */
public class EmbeddedFees2RegisterServiceApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .sources(FeesRegisterServiceApplication.class)
            .profiles("embedded")
            .run();
    }
}
