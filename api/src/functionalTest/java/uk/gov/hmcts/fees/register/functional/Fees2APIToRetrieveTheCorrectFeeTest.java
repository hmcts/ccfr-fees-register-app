package uk.gov.hmcts.fees.register.functional;

import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees.register.functional.dsl.FeesRegisterTestDsl;

import java.util.List;

@RunWith(SpringIntegrationSerenityRunner.class)
public class Fees2APIToRetrieveTheCorrectFeeTest extends IntegrationTestBase {
    @Autowired
    private FeesRegisterTestDsl scenario;

    @Test
    public void findAllchannelTypes() {
        scenario.given()
                .when().getAllchannelTypes()
                .then().got(List.class, (result -> Assertions.assertThat(result).isNotEmpty()));
    }

    @Test
    public void findAllDirectionTypes() {
        scenario.given()
                .when().getAllDirectionTypes()
                .then().got(List.class, (result -> Assertions.assertThat(result).isNotEmpty()));
    }

    @Test
    public void findAllEventTypes() {
        scenario.given()
                .when().getAllEventTypes()
                .then().got(List.class, (result -> Assertions.assertThat(result).isNotEmpty()));
    }

    @Test
    public void findAllJurisdictions1Types() {
        scenario.given()
                .when().getAllJurisdictions1Types()
                .then().got(List.class, (result -> Assertions.assertThat(result).isNotEmpty()));
    }

    @Test
    public void findAllJurisdictions2Types() {
        scenario.given()
                .when().getAllJurisdictions2Types()
                .then().got(List.class, (result -> Assertions.assertThat(result).isNotEmpty()));
    }

    @Test
    public void findAllServiceTypes() {
        scenario.given()
                .when().getAllServiceTypes()
                .then().got(List.class, (result -> Assertions.assertThat(result).isNotEmpty()));
    }
}
