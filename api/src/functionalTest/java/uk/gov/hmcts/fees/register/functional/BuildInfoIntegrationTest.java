package uk.gov.hmcts.fees.register.functional;

import com.fasterxml.jackson.databind.JsonNode;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees.register.functional.dsl.FeesRegisterTestDsl;

import java.io.IOException;

@Ignore
public class BuildInfoIntegrationTest extends IntegrationTestBase{

    @Autowired
    private FeesRegisterTestDsl scenario;

    @Test
    public void buildInfoShouldBePresent() throws IOException, NoSuchFieldException {
        scenario.given()
                .when().getBuildInfo()
                .then().got(JsonNode.class, response -> {
                    Assertions.assertThat(response.at("/git/commit/id").asText()).isNotEmpty();
                    Assertions.assertThat(response.at("/build/version").asText()).isNotEmpty();
                });
    }
}
