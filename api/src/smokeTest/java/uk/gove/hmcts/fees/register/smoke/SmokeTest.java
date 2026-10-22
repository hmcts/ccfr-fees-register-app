package uk.gove.hmcts.fees.register.smoke;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import static org.hamcrest.Matchers.equalTo;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RunWith(SpringIntegrationSerenityRunner.class)
@Slf4j
public class SmokeTest {
    @Value("${TEST_URL:http://localhost:8080}")
    private String testUrl;

    @Before
    public void setup() {
        RestAssured.baseURI = testUrl;
        log.info("Fees-Register-Api base url is :{}", testUrl);
    }

    @Test
    public void healthCheck() {
        SerenityRest.given()
            .relaxedHTTPSValidation()
            .header(CONTENT_TYPE, "application/json")
            .when()
            .get("/health")
            .then()
            .statusCode(200)
            .body("status", equalTo("UP"));
    }

    @Test
    public void getApprovedFees() {
        SerenityRest.given()
            .relaxedHTTPSValidation()
            .header(CONTENT_TYPE, "application/json")
            .when()
            .get("/fees-register/approvedFees")
            .then()
            .statusCode(200);
    }
}
