package uk.gov.hmcts.fees.register.functional;

import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static uk.gov.hmcts.fees.register.functional.fixture.FixedFeeFixture.aFixedFee;

public class DeleteFeeTest extends IntegrationTestBase {

    @Test
    public void shouldDeleteAnApprovedFee() {
        // editor creates a fee
        Response response = feeService.createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = response.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];

        assertThat(feeCode).isNotBlank();

        // editor submits a fee for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, 1)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver approves a fee
        feeService.approveAFee(userBootstrap.getApprover(), feeCode, 1)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // editor try to delete an approved fee - fail 403 Forbidden
        feeService.deleteAFee(userBootstrap.getEditor(), feeCode)
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value());

        // admin deletes an approved fee - success
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // verify with get - fail 404 not found
        feeService.getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

}
