package uk.gov.hmcts.fees.register.functional;

import io.restassured.response.Response;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.fees.register.functional.dsl.FeesRegisterTestDsl;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static uk.gov.hmcts.fees.register.functional.fixture.FixedFeeFixture.aFixedFee;
import static uk.gov.hmcts.fees.register.functional.service.FeeService.getLatestFeeVersion;

@RunWith(SpringIntegrationSerenityRunner.class)
public class UpdateFeeTest extends IntegrationTestBase {

    @Autowired
    private FeesRegisterTestDsl scenario;

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
    public void should_not_update_a_fee_after_submission_by_non_freg_editor_role_user() {
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

        //Ammending Should not take place by non freg-editor role user
        Response amendResponse1 = feeService.amendAFeeVersion(userBootstrap.getApprover(), feeCode, feeVersionDto);
        amendResponse1.then()
            .statusCode(HttpStatus.FORBIDDEN.value());

        // admin deletes a fee - success
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

    @Test
    public void approvedFeesShouldNotIncludeDuplicateFees() {
        FixedFeeDto fixedFeeDto = aFixedFee();
        fixedFeeDto.getVersion().setValidFrom(DateUtils.addDays(new Date(), -50));
        fixedFeeDto.getVersion().setValidTo(DateUtils.addDays(new Date(), -2));

        // Create a fee with a past validFrom and validTo date
        Response response = feeService.createAFee(userBootstrap.getEditor(), fixedFeeDto);
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

        // Amending the amount and version number should be incremented by 1
        feeVersionDto.setVersion(2);
        feeVersionDto.setFlatAmount(new FlatAmountDto(BigDecimal.valueOf(600.00)));
        feeVersionDto.setReasonForUpdate("Updating the amount to 600.00");
        feeVersionDto.setValidFrom(new Date());
        feeVersionDto.setValidTo(DateUtils.addDays(new Date(), 30));
        Response amendResponse1 = feeService.createANewFeeVersion(userBootstrap.getEditor(), feeCode, feeVersionDto);
        amendResponse1.then()
            .statusCode(HttpStatus.CREATED.value());

        // editor submits a new fee version for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver approves a new fee version
        feeService.approveAFee(userBootstrap.getApprover(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        scenario.given()
            .when().getApprovedFees()
            .then().ok().got(Fee2Dto[].class, feeDtos -> {
                Assertions.assertThat(feeDtos)
                    .filteredOn(feeDto -> feeCode.equals(feeDto.getCode()))
                    .hasSize(1)
                    .hasOnlyElementsOfType(Fee2Dto.class)
                    .extracting(feeDto -> feeDto) // Converts the result to a list format
                    .first()
                    .satisfies(feeDto -> {
                        Assertions.assertThat(feeDto).isNotNull();
                        Assertions.assertThat(feeDto.getCurrentVersion().getVersion())
                            .isEqualTo(2);
                        Assertions.assertThat(feeDto.getCurrentVersion().getReasonForUpdate())
                            .isEqualTo(feeVersionDto.getReasonForUpdate());
                        Assertions.assertThat(feeDto.getCurrentVersion().getValidFrom())
                            .isToday();
                        Assertions.assertThat(feeDto.getCurrentVersion().getValidTo())
                            .isAfter(new Date());
                    });
            });

        // admin deletes an approved fee - success
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    public void scheduledApprovedFeeShouldNotOverrideADiscontinuedFee() {
        FixedFeeDto fixedFeeDto = aFixedFee();
        fixedFeeDto.getVersion().setValidFrom(DateUtils.addDays(new Date(), -50));
        fixedFeeDto.getVersion().setValidTo(DateUtils.addDays(new Date(), -2));

        // Create a fee with a past validFrom and validTo date
        Response response = feeService.createAFee(userBootstrap.getEditor(), fixedFeeDto);
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

        // Amending the amount and version number should be incremented by 1
        feeVersionDto.setVersion(2);
        feeVersionDto.setFlatAmount(new FlatAmountDto(BigDecimal.valueOf(600.00)));
        feeVersionDto.setReasonForUpdate("Updating the amount to 600.00");
        feeVersionDto.setValidFrom(DateUtils.addDays(new Date(), 1));
        feeVersionDto.setValidTo(DateUtils.addDays(new Date(), 30));
        Response amendResponse1 = feeService.createANewFeeVersion(userBootstrap.getEditor(), feeCode, feeVersionDto);
        amendResponse1.then()
            .statusCode(HttpStatus.CREATED.value());

        // editor submits a new fee version for review
        feeService.submitAFee(userBootstrap.getEditor(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // approver approves a new fee version
        feeService.approveAFee(userBootstrap.getApprover(), feeCode, feeVersionDto.getVersion())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        scenario.given()
            .when().getApprovedFees()
            .then().ok().got(Fee2Dto[].class, feeDtos -> {
                Assertions.assertThat(feeDtos)
                    .filteredOn(feeDto -> feeCode.equals(feeDto.getCode()))
                    .hasSize(1)
                    .hasOnlyElementsOfType(Fee2Dto.class)
                    .extracting(feeDto -> feeDto) // Converts the result to a list format
                    .first()
                    .satisfies(feeDto -> {
                        Assertions.assertThat(feeDto).isNotNull();
                        Assertions.assertThat(feeDto.getCurrentVersion().getVersion())
                            .isEqualTo(1);
                        Assertions.assertThat(feeDto.getCurrentVersion().getValidFrom())
                            .isBefore(new Date());
                        Assertions.assertThat(feeDto.getCurrentVersion().getValidTo())
                            .isBefore(new Date());
                    });
            });

        // admin deletes an approved fee - success
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    public void scheduledApprovedFeesShouldNotAppearInApprovedFeesList() {
        FixedFeeDto fixedFeeDto = aFixedFee();
        fixedFeeDto.getVersion().setValidFrom(DateUtils.addDays(new Date(), 1));
        fixedFeeDto.getVersion().setValidTo(DateUtils.addDays(new Date(), 30));

        // Create a fee with a future validFrom date
        Response response = feeService.createAFee(userBootstrap.getEditor(), fixedFeeDto);
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

        scenario.given()
            .when().getApprovedFees()
            .then().ok().got(Fee2Dto[].class, feeDtos -> {
                Assertions.assertThat(feeDtos)
                    .filteredOn(feeDto -> feeCode.equals(feeDto.getCode()))
                    .hasSize(0);
            });

        // admin deletes an approved fee - success
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }


    @Test
    public void feesPendingApprovalShouldNotAppearInApprovedFeesList() {
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

        scenario.given()
            .when().getApprovedFees()
            .then().ok().got(Fee2Dto[].class, feeDtos -> {
                Assertions.assertThat(feeDtos)
                    .filteredOn(feeDto -> feeCode.equals(feeDto.getCode()))
                    .hasSize(0);
            });

        // admin deletes an approved fee - success
        feeService.deleteAFee(userBootstrap.getAdmin(), feeCode)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }


}
