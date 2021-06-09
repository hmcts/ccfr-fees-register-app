package uk.gov.hmcts.fees.register.functional;

import io.restassured.response.Response;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static uk.gov.hmcts.fees.register.functional.fixture.FixedFeeFixture.aFixedFee;
import static uk.gov.hmcts.fees.register.functional.fixture.FixedFeeFixture.aModifiedFee;

@RunWith(SpringIntegrationSerenityRunner.class)
@Ignore("Ignoring this Test as part of PAY-4634 As this is a Test to be handed over to the Feature teams.....")
public class UpdateFeeTest extends IntegrationTestBase {

    @Test
    public void should_update_an_approved_fee_before_submission() {
        // editor creates a fee
        FixedFeeDto fixedFeeDto = aFixedFee();
        Response response = feeService.createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = response.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        fixedFeeDto.setCode(feeCode);

        assertThat(feeCode).isNotBlank();

        //Ammending only the Amount,automatically increments the version
        Response amendResponse1 = feeService.amendAFee(userBootstrap.getEditor(), aModifiedFee(fixedFeeDto));
        amendResponse1.then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //Ammending only the Code,automatically increments the version
        fixedFeeDto.setCode("FEE0573");
        Response amendResponse2 = feeService.amendAFee(userBootstrap.getEditor(), aModifiedFee(fixedFeeDto));
        amendResponse2.then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // editor submits a fee for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, 1)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver approves a fee
        feeService.approveAFee(userBootstrap.getApprover(), feeCode, 1)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // admin deletes an approved fee - success
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // verify with get - fail 404 not found
        feeService.getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_not_update_an_approved_fee_after_submission() {
        // editor creates a fee
        FixedFeeDto fixedFeeDto = aFixedFee();
        Response response = feeService.createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = response.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        fixedFeeDto.setCode(feeCode);

        assertThat(feeCode).isNotBlank();

        // editor submits a fee for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, 1)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //Ammending only the Amount,automatically increments the version
        Response amendResponse1 = feeService.amendAFee(userBootstrap.getEditor(), aModifiedFee(fixedFeeDto));
        amendResponse1.then()
            .statusCode(HttpStatus.BAD_REQUEST.value());

        // approver approves a fee
        feeService.approveAFee(userBootstrap.getApprover(), feeCode, 1)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // admin deletes an approved fee - success
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // verify with get - fail 404 not found
        feeService.getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_not_update_an_approved_fee_after_approval() {
        // editor creates a fee
        FixedFeeDto fixedFeeDto = aFixedFee();
        Response response = feeService.createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = response.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        fixedFeeDto.setCode(feeCode);

        assertThat(feeCode).isNotBlank();

        // editor submits a fee for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, 1)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver approves a fee
        feeService.approveAFee(userBootstrap.getApprover(), feeCode, 1)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //Ammending only the Amount,automatically increments the version
        Response amendResponse1 = feeService.amendAFee(userBootstrap.getEditor(), aModifiedFee(fixedFeeDto));
        amendResponse1.then()
            .statusCode(HttpStatus.BAD_REQUEST.value());

        // admin deletes an approved fee - success
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // verify with get - fail 404 not found
        feeService.getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_update_an_approved_fee_after_rejection() {
        // editor creates a fee
        FixedFeeDto fixedFeeDto = aFixedFee();
        Response response = feeService.createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = response.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        fixedFeeDto.setCode(feeCode);

        assertThat(feeCode).isNotBlank();

        // editor submits a fee for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, 1)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver rejects the Submitted fee
        feeService.rejectAFee(userBootstrap.getApprover(), fixedFeeDto)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //Ammending only the Amount,automatically increments the version
        Response amendResponse1 = feeService.amendAFee(userBootstrap.getEditor(), aModifiedFee(fixedFeeDto));
        amendResponse1.then()
            .statusCode(HttpStatus.NO_CONTENT.value());

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
