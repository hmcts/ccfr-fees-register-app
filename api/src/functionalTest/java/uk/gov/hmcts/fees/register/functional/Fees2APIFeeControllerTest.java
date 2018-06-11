package uk.gov.hmcts.fees.register.functional;

import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees.register.api.contract.*;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
//import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees.register.functional.dsl.FeesRegisterTestDsl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.rangeGroupDtoWith;
import static uk.gov.hmcts.fees2.register.api.contract.request.CreateFeeDto.*;
import static uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto.*;
//import static uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto.*;
import static uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto.*;

public class Fees2APIFeeControllerTest extends IntegrationTestBase {

    @Autowired
    private FeesRegisterTestDsl scenario;

    private String feeCode;
    private String feeCode1;
    private String feeCode2;

    CreateRangedFeeDto proposeRangedFees;//new CreateRangedFeeDto("X0011", version, "civil","county court", "civil money claims", "online", "enhanced","issue", "Test memo line", "CMC online fee order name","Natural code 001", new BigDecimal(1), new BigDecimal(10000));

    CreateFixedFeeDto proposeFixedFees;

    //  ApproveFeeDto approveFeeCode;
    CreateFeeDto createFeeDto;

  /*  @Test
    public void createRangedFees201() throws IOException {

        feeCode = UUID.randomUUID().toString();

        proposeRangedFees = getRangedFeeDto(feeCode);
      //  approveFeeCode = getApproveFeeDto(feeCode);
        createFeeDto = getCreateFeeDto(feeCode);

        scenario.given().userId("1")
                .when().createRangedFee(proposeRangedFees)
                .then().noContent().getFeeCodeAndVerifyStatus1(feeCode).got(Fee2Dto.class, fee2Dtos -> {
            assertThat(fee2Dtos.getFeeVersionDtos().get(0).getStatus()).isEqualTo(FeeVersionStatus.approved);
        }).deleteFeeCode1(feeCode).isDeleted();
    }

    private CreateRangedFeeDto getRangedFeeDto(String feeCode) {
        //feeCode = UUID.randomUUID().toString();
        FlatAmountDto amount = new FlatAmountDto(new BigDecimal(100));

        MutableDateTime validTo = new MutableDateTime(new Date());
        validTo.addDays(90);

        FeeVersionDto version = new FeeVersionDto(1, new Date(), validTo.toDate(), "Testing", FeeVersionStatus.draft, amount, null);
        return new CreateRangedFeeDto(feeCode, version, "civil","family court", "civil money claims", "online", "enhanced","issue", "Test memo line", "CMC online fee order name","Natural code 001", new BigDecimal(10000), new BigDecimal(1));
    }

  //  private ApproveFeeDto getApproveFeeDto(String feeCode){
    //    return new ApproveFeeDto(feeCode, 1);
   // }

    private CreateFeeDto getCreateFeeDto(String feeCode){
        return new CreateFeeDto(feeCode, 1);
    }

    @Ignore
    @Test
    public void createFixedFees201() throws IOException {

        feeCode1 = UUID.randomUUID().toString();
        proposeFixedFees = getFixedFeeDto(feeCode1);

        scenario.given().userId("1")
                .when().createFixedFee(proposeFixedFees)
                .then().isCreated()
                .deleteFeeCode1(feeCode1).isDeleted();
    }

    private CreateFixedFeeDto getFixedFeeDto(String feeCode) {
        FlatAmountDto amount = new FlatAmountDto(new BigDecimal(550));
        DateTime to = new DateTime();
        to.plusDays(90);
        FeeVersionDto version = new FeeVersionDto(1, new Date(), to.toDate(), "Testing", FeeVersionStatus.approved, amount, null);
        //CreateFixedFeeDto createfixedfee = new CreateFixedFeeDto(feeCode, version, "family","family court", "divorce", "online", "enhanced","issue", "Test memo line", "CMC online fee order name","Natural code 001");
        CreateFixedFeeDto createfixedfee = new CreateFixedFeeDto(feeCode, version, "family","family court", "divorce", "online", "enhanced","issue", "Test memo line", "feeOrderName","naturalAccountCode","statutory","CMC online fee order name",false);
        createfixedfee.setUnspecifiedClaimAmount(false);
        return createfixedfee;
    } */

    @Test
    public void getlookupresponseMessageForDivorce() throws IOException {

        scenario.given()
            .when().getLookUpResponse("divorce", "family", "family court", "default", "issue")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0002");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("550.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault1() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 0.1)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0202");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("35.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault2() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 300)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0202");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("35.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault3() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 300.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0203");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("50.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault4() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 500)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0203");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("50.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault5() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 500.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0204");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("70.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault6() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 1000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0204");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("70.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault7() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 1000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0205");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("80.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault8() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 1500)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0205");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("80.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault9() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 1500.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0206");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("115.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault10() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 3000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0206");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("115.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault11() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 3000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0207");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("205.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault12() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 5000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0207");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("205.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault13() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 5000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0208");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("455.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault14() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 10000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0208");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("455.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault15() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 10000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0209");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("500.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault16() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 200000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0209");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("10000.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault16_1() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 100999)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0209");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("5049.95");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault17() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 200000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0210");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("10000.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCDefault18() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "issue", 300000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0210");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("10000.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline1() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 0.1)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0211");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("25.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline2() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 300)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0211");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("25.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline3() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 300.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0212");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("35.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline4() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 500)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0212");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("35.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline5() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 500.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0213");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("60.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline6() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 1000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0213");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("60.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline7() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 1000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0214");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("70.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline8() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 1500)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0214");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("70.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline9() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 1500.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0215");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("105.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline10() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 3000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0215");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("105.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline11() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 3000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0216");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("185.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline12() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 5000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0216");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("185.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline13() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 5000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0217");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("410.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline14() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 10000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0217");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("410.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCOnline15() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "online", "issue", 10000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0218");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("450.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing1() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 0.1)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0221");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("25.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing2() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 300)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0221");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("25.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing3() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 300.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0222");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("55.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing4() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 500)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0222");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("55.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing5() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 500.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0183");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("80.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing6() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 1000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0183");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("80.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing7() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 1000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0223");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("115.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing8() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 1500)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0223");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("115.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing9() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 1500.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0224");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("170.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing10() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 3000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0224");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("170.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing11() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 3000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0225");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("335.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing12() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 10000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0225");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("335.00");
        });
    }

    @Test
    public void getlookupresponseMessageForProbate() throws IOException {

        scenario.given()
            .when().getLookUpForProbateResponse("probate", "family", "probate registry", "default", "issue", "all", new BigDecimal("5000.01"))
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0219");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("155.00");
        });
    }

    @Test
    public void getlookupresponseMessageForProbateCopies() throws IOException {

        scenario.given()
            .when().getLookUpForProbateResponse("probate", "family", "probate registry", "default", "copies", "all", new BigDecimal("5000"))
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0003");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("2500.00");
        });
    }

    @Test
    public void getlookupresponseMessageForProbateCopies2() throws IOException {

        scenario.given()
            .when().getLookUpForProbateResponse("probate", "family", "probate registry", "default", "copies", "all", new BigDecimal("1"))
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0003");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("0.50");
        });
    }

    @Test
    public void getlookupresponseMessageForProbateApplicatTypePersonal() throws IOException {

        scenario.given()
            .when().getLookUpForProbateResponse("probate", "family", "probate registry", "default", "issue", "personal", new BigDecimal("5000.01"))
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0226");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("215.00");
        });
    }

    @Ignore
    @Test
    public void getlookupresponseMessage1() throws IOException {

        scenario.given().userId("1")
            .when().getLookUpResponse("divorce", "family", "family court", "default", "issue")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("X0165");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("550.00");
        });
    }

   /* @Ignore
    @Test
    public void createFixedFeesForUnspecifiedAmount201() throws IOException {

        feeCode2 = UUID.randomUUID().toString();
        proposeFixedFees = getFixedFeeUnspecifiedDto(feeCode2);

        scenario.given().userId("1")
                .when().createFixedFee(proposeFixedFees)
                .then().isCreated()
                .deleteFeeCode1(feeCode2).isDeleted();
    }

    private CreateFixedFeeDto getFixedFeeUnspecifiedDto(String feeCode2) {
        FlatAmountDto amount = new FlatAmountDto(new BigDecimal(550));
        DateTime to = new DateTime();
        to.plusDays(90);
        FeeVersionDto version = new FeeVersionDto(1, new Date(), to.toDate(), "Testing", FeeVersionStatus.approved, amount, null);
        CreateFixedFeeDto createfixedfeeforunspecified = new CreateFixedFeeDto(feeCode2, version, "family","family court", "divorce", "online", "enhanced","issue", "Test memo line","feeOrderName","naturalAccountCode", "CMC online fee order name","siRefId",true);
        createfixedfeeforunspecified.setUnspecifiedClaimAmount(true);
        return createfixedfeeforunspecified;
    }*/

}
