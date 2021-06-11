package uk.gov.hmcts.fees.register.functional.service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.functional.idam.models.User;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FeeDto;

import java.math.BigDecimal;
import java.util.List;

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
            .patch("/fees/{feeCode}/versions/{version}/approve", feeCode, version);
    }

    public static Response deleteAFee(User user, String feeCode) {
        return RestAssured.given()
            .header(HttpHeaders.AUTHORIZATION, user.getAuthorisationToken())
            .when()
            .delete("/fees-register/fees/{feeCode}", feeCode);
    }

    public static Response getAFee(String feeCode) {
        return RestAssured.given()
            .when()
            .get("/fees-register/fees/{feeCode}", feeCode);
    }

    public static Response createAFee(User editor, FeeDto feeDto) {
        return RestAssured
            .given()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, editor.getAuthorisationToken())
            .body(feeDto)
            .when()
            .post("/fees-register/fixed-fees");
    }

    public static Response amendAFeeVersion(User editor, String feeCode, FeeVersionDto feeVersionDto) {
        return RestAssured
            .given()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, editor.getAuthorisationToken())
            .body(feeVersionDto)
            .when()
            .put("/fees/{code}/versions/{version}", feeCode, feeVersionDto.getVersion());
    }

    public static Response rejectAFee(User approver, FeeDto feeDto) {
        return RestAssured
            .given()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, approver.getAuthorisationToken())
            .body(feeDto)
            .when()
            .put("/fees/{feeCode}/versions/{version}/reject", feeDto.getCode(), feeDto.getVersion().getVersion());
    }

    public static Response rejectAFeeVersion(User approver, Fee2Dto fee2Dto) {

        return RestAssured
            .given()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, approver.getAuthorisationToken())
            .body(fee2Dto)
            .when()
            .put("/fees/{feeCode}/versions/{version}/reject", fee2Dto.getCode(), getLatestFeeVersion(fee2Dto).getVersion());
    }

    @NotNull
    public static FeeVersionDto getLatestFeeVersion(Fee2Dto fee2Dto) {
        List<FeeVersionDto> listOfFeeVersionDTOs = fee2Dto.getFeeVersionDtos();
        FeeVersionDto feeVersionDto = listOfFeeVersionDTOs.get(listOfFeeVersionDTOs.size() - 1);
        feeVersionDto.setFlatAmount(new FlatAmountDto((BigDecimal.valueOf(500))));
        return feeVersionDto;
    }
}
