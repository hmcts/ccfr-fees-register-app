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
        response = feeService.submitAFee(userBootstrap.getEditor(), feeCode, 1);
        response.then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver approves a fee
        response = feeService.approveAFee(userBootstrap.getApprover(), feeCode, 1);
        response.then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // admin deletes a fee an approved fee
        response = feeService.deleteAFee(userBootstrap.getAdmin(), feeCode);
        response.then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
