package uk.gov.hmcts.fees.register.functional;

import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees.register.functional.dsl.FeesRegisterTestDsl;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;

import java.io.IOException;
import java.math.BigDecimal;

import static java.lang.Double.parseDouble;

@RunWith(SpringIntegrationSerenityRunner.class)
public class Fees2APIFeeControllerTest extends IntegrationTestBase {

    @Autowired
    private FeesRegisterTestDsl scenario;

    @Test
    public void getlookupresponseMessageForDivorce() throws IOException {

        scenario.given()
            .when().getLookUpResponse("divorce", "family", "family court", "default", "issue")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0002");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("593.00");
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
    public void getlookupresponseMessageForCMCHearing1() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 0.1)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0221");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("27.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing2() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 300)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0221");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("27.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing3() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 300.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0222");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("59.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing4() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 500)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0222");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("59.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing5() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 500.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0183");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("85.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing6() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 1000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0183");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("85.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing7() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 1000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0223");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("123.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing8() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 1500)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0223");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("123.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing9() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 1500.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0224");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("181.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing10() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 3000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0224");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("181.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing11() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 3000.01)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0225");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("346.00");
        });
    }

    @Test
    public void getlookupresponseMessageForCMCHearing12() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponse("civil money claims", "civil", "county court", "default", "hearing", 10000)
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0225");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("346.00");
        });
    }

    @Test
    public void getlookupresponseMessageForProbate() throws IOException {

        scenario.given()
            .when().getLookUpForProbateResponseWithKeywordApplicationType("probate", "family", "probate registry", "default", "issue", "all", parseDouble("5000.01"),"SA")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0219");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("155.00");
        });
    }

    @Test
    public void getlookupresponseMessageForProbateCopies() throws IOException {

        scenario.given()
            .when().getLookUpForProbateResponseCopiesGrantWill("probate", "family", "probate registry", "default", "copies", "all", new BigDecimal("5000"),"GrantWill")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0546");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("7500.00");
        });
    }

    @Test
    public void getlookupresponseMessageForProbateCopies2() throws IOException {

        scenario.given()
            .when().getLookUpForProbateResponseCopiesGrantWill("probate", "family", "probate registry", "default", "copies", "all", new BigDecimal("1"),"GrantWill")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0546");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("1.50");
        });
    }

    @Test
    public void getlookupresponseMessageForProbateApplicatTypePersonal() throws IOException {

        scenario.given()
            .when().getLookUpForProbateResponseWithKeywordApplicationType("probate", "family", "probate registry", "default", "issue", "personal",5000.01, "PA")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0226");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("215.00");
        });
    }

    @Test
    public void getLookupResponseForProbateFeeWithMaxRangeAs5000() {

        scenario.given()
            .when().getLookUpResponsewithkeywordAmount("probate", "family", "probate registry", "default", "issue", "PAL5K", 5000)
            .then().ok().got(FeeLookupResponseDto.class, feeLookupResponseDto -> {
                Assertions.assertThat(feeLookupResponseDto.getDescription()).isEqualTo("Personal Application for grant of Probate");
                Assertions.assertThat(feeLookupResponseDto.getVersion()).isNotNull();
                Assertions.assertThat(feeLookupResponseDto.getFeeAmount()).isEqualTo("0.00");
        });
    }

    @Test
    public void getlookupresponseMessageForProbateCopiesGrantWill() throws IOException {

        scenario.given()
            .when().getLookUpForProbateResponseCopiesGrantWill("probate", "family", "probate registry", "default", "copies", "all", new BigDecimal("2"),"GrantWill")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("3.00");
        });
    }

    @Test
    public void getLookupResponseForProbateFeeWithCaveat() {

        scenario.given()
            .when().getLookUpResponsewithKeywordFixedFee("probate", "family", "probate registry", "default", "miscellaneous", "Caveat")
            .then().ok().got(FeeLookupResponseDto.class, feeLookupResponseDto -> {
            Assertions.assertThat(feeLookupResponseDto.getDescription()).isEqualTo("Application for the entry or extension of a caveat");
            Assertions.assertThat(feeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(feeLookupResponseDto.getFeeAmount()).isEqualTo("3.00");
        });
    }

    @Test
    public void getLookupResponseForProbateFeeWithKeywordSAL5K() {

        scenario.given()
            .when().getLookUpResponsewithkeywordAmount("probate", "family", "probate registry", "default", "issue", "SAL5K", 5000)
            .then().ok().got(FeeLookupResponseDto.class, feeLookupResponseDto -> {
            Assertions.assertThat(feeLookupResponseDto.getDescription()).isEqualTo("Application for a grant of probate (Estate under 5000 GBP)");
            Assertions.assertThat(feeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(feeLookupResponseDto.getFeeAmount()).isEqualTo("0.00");
        });
    }

    @Test
    public void getLookupResponseForProbateFeeWithKeywordSA() {

        scenario.given()
            .when().getLookUpResponsewithkeywordAmount("probate", "family", "probate registry", "default", "issue", "SA", 5000.01)
            .then().ok().got(FeeLookupResponseDto.class, feeLookupResponseDto -> {
            Assertions.assertThat(feeLookupResponseDto.getDescription()).isEqualTo("Application for a grant of probate (Estate over 5000 GBP)");
            Assertions.assertThat(feeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(feeLookupResponseDto.getFeeAmount()).isEqualTo("155.00");
        });
    }

    @Test
    public void getlookupresponseMessageForFPL() throws IOException {

        scenario.given()
            .when().getLookUpResponsewithkeyword("public law", "family", "family court", "default", "issue", "CareOrder")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0313");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Application for proceedings under Section 31 of Act");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("2215.00");
        });
    }
}
