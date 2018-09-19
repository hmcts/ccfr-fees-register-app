package uk.gov.hmcts.fees.register.functional.service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.functional.idam.models.User;

import static uk.gov.hmcts.fees.register.functional.fixture.FixedFeeFixture.aFixedFee;


@Component
public class FeeService {

    public static Response submitAFee(User editor, String feeCode, int version) {
        return RestAssured.given()
            .header(HttpHeaders.AUTHORIZATION, editor.getAuthorisationToken())
            .when()
            .patch("/fees/{feeCode}/versions/{version}/submit-for-review", feeCode, version);
    }

    public static Response approveAFee(User approver, String feeCode, int version) {
        return RestAssured.given()
            .header(HttpHeaders.AUTHORIZATION, approver.getAuthorisationToken())
            .when()
            .patch("/fees/{feeCode}/versions/{version}/submit-for-review", feeCode, version);
    }

    public static Response deleteAFee(User user, String feeCode) {
        return RestAssured.given()
            .header(HttpHeaders.AUTHORIZATION, user.getAuthorisationToken())
            .when()
            .delete("/fees-register/fees/{feeCode}", feeCode);
    }

    public static Response createAFee(User editor, String feeJson) {
        return RestAssured
            .given()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, editor.getAuthorisationToken())
            .body(feeJson)
            .when()
            .post("/fees-register/fixed-fees");
    }
}
