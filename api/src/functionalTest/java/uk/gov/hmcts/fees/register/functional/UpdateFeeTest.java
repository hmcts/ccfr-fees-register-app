package uk.gov.hmcts.fees.register.functional;

import io.restassured.response.Response;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static uk.gov.hmcts.fees.register.functional.fixture.FixedFeeFixture.aFixedFee;
import static uk.gov.hmcts.fees.register.functional.service.FeeService.getLatestFeeVersion;

@RunWith(SpringIntegrationSerenityRunner.class)
public class UpdateFeeTest extends IntegrationTestBase {

    @Test
    public void should_update_a_created_fee_version_twice_before_submission() {

        Response response = feeService.createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = response.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        assertThat(feeCode).isNotBlank();

        //Retrieve the Created Fee...
        Fee2Dto fee2Dto = feeService.getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.OK.value()).extract().as(Fee2Dto.class);


        //Ammending only the Amount
        FeeVersionDto feeVersionDto = getLatestFeeVersion(fee2Dto);
        Response amendResponse1 = feeService.amendAFeeVersion(userBootstrap.getEditor(), feeCode, feeVersionDto);
        amendResponse1.then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //Retrieve the Amended Fee...
        Fee2Dto fee2DtoAmended = feeService.getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.OK.value()).extract().as(Fee2Dto.class);

        //Ammending only the Amount
        FeeVersionDto amendedFeeVersionDto = getLatestFeeVersion(fee2DtoAmended);
        Response amendResponse2 = feeService.amendAFeeVersion(userBootstrap.getEditor(), feeCode, amendedFeeVersionDto);
        amendResponse2.then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // editor submits a fee for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, amendedFeeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver approves a fee
        feeService.approveAFee(userBootstrap.getApprover(), feeCode, amendedFeeVersionDto.getVersion())
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
    public void should_not_update_a_fee_after_approval() {

        Response response = feeService.createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = response.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        assertThat(feeCode).isNotBlank();

        //Retrieve the Created Fee...
        Fee2Dto fee2Dto = feeService.getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.OK.value()).extract().as(Fee2Dto.class);

        // editor submits a fee for review
        FeeVersionDto feeVersionDto = getLatestFeeVersion(fee2Dto);
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver approves a fee
        feeService.approveAFee(userBootstrap.getApprover(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //Ammending should take place post Approval
        Response amendResponse1 = feeService.amendAFeeVersion(userBootstrap.getEditor(), feeCode, feeVersionDto);
        amendResponse1.then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // admin deletes an approved fee - success
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    public void should_update_a_fee_after_submission() {
        Response response = feeService.createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = response.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        assertThat(feeCode).isNotBlank();

        //Retrieve the Created Fee...
        Fee2Dto fee2Dto = feeService.getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.OK.value()).extract().as(Fee2Dto.class);

        // editor submits a fee for review
        FeeVersionDto feeVersionDto = getLatestFeeVersion(fee2Dto);
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //Ammending Should take place post Submission via APi but disabled from UI.
       Response amendResponse1 = feeService.amendAFeeVersion(userBootstrap.getEditor(), feeCode, feeVersionDto);
        amendResponse1.then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // admin deletes an approved fee - success
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

    }


    @Test
    public void should_update_a_fee_after_rejection() {

        Response response = feeService.createAFee(userBootstrap.getEditor(), aFixedFee());
        String feeCode = response.then()
            .statusCode(HttpStatus.CREATED.value())
            .and()
            .extract().header(HttpHeaders.LOCATION).split("/")[3];
        assertThat(feeCode).isNotBlank();

        //Retrieve the Created Fee...
        Fee2Dto fee2Dto = feeService.getAFee(feeCode)
            .then()
            .statusCode(HttpStatus.OK.value()).extract().as(Fee2Dto.class);

        // editor submits a fee for review
        FeeVersionDto feeVersionDto = getLatestFeeVersion(fee2Dto);
        // editor submits a fee for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver rejects the Submitted fee
        feeService.rejectAFeeVersion(userBootstrap.getApprover(), fee2Dto)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        //Ammending only the Amount,automatically increments the version
        Response amendResponse1 = feeService.amendAFeeVersion(userBootstrap.getEditor(), feeCode, feeVersionDto);
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
