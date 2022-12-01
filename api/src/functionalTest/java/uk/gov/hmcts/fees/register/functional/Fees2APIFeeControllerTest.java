package uk.gov.hmcts.fees.register.functional;

import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
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
            .when().getLookUpResponseWithKeyword("divorce", "family", "family court", "default", "issue", "DivorceCivPart")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0002");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("593.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_hearing_min_range_FEE0183() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            500.01, "HearingSmallClaims")

            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0183");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (exceeds £500 but not £1,000)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("85.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_hearing_max_range_FEE0183() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            1000, "HearingSmallClaims")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0183");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (exceeds £500 but not £1,000)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("85.00");
        });
    }


    @Test
    public void get_lookup_for_cmc_min_range_FEE0202() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims", "civil", "county court", "default", "issue", 0.1, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0202");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 0.01 up to 300 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("35.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_max_range_FEE0202() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            300, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0202");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 0.01 up to 300 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("35.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_min_range_FEE0203() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            300.01, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0203");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 300.01 up to 500 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("50.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_max_range_FEE0203() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            500, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0203");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 300.01 up to 500 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("50.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_min_range_FEE0204() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            500.01, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0204");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 500.01 up to 1000 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("70.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_max_range_FEE0204() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            1000, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0204");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 500.01 up to 1000 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("70.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_min_range_FEE0205() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            1000.01, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0205");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 1000.01 up to 1500 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("80.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_max_range_FEE0205() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            1500, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0205");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 1000.01 up to 1500 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("80.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_min_range_FEE0206() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            1500.01,"MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0206");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 1500.01 up to 3000 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("115.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_max_range_FEE0206() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            3000, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0206");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 1500.01 up to 3000 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("115.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_min_range_FEE0207() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            3000.01, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0207");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 3000.01 up to 5000 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("205.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_max_range_FEE0207() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            5000, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0207");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - 3000.01 up to 5000 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("205.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_min_range_FEE0208() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            5000.01,"MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0208");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Money Claims - Claim Amount - 5000.01 up to 10000 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("455.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_max_range_FEE0208() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            10000, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0208");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Money Claims - Claim Amount - 5000.01 up to 10000 GBP");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("455.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_min_range_FEE0209() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            10000.01, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0209");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Money Claims - Claim Amount - 10000.01 up to 200000 GBP. FEE AMOUNT = 5% of claim value");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("500.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_max_range_FEE0209() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            200000, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0209");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Money Claims - Claim Amount - 10000.01 up to 200000 GBP. FEE AMOUNT = 5% of claim value");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("10000.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_within_range_FEE0209() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            100999, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0209");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Money Claims - Claim Amount - 10000.01 up to 200000 GBP. FEE AMOUNT = 5% of claim value");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("5049.95");
        });
    }

    @Test
    public void get_lookup_for_cmc_min_range_FEE0210() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            200000.01, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0210");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Money Claims - Claim Amount - 200000.01 GBP or more");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("10000.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_above_min_range_FEE0210() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "issue",
            300000.01, "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0210");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Money Claims - Claim Amount - 200000.01 GBP or more");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("10000.00");
        });
    }


    @Test
    public void get_lookup_for_cmc_hearing_min_range_FEE0221() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            0.01, "HearingSmallClaims")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0221");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (does not exceed 300 GBP)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("27.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_hearing_max_range_FEE0221() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default",
            "hearing", 300, "HearingSmallClaims")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0221");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (does not exceed 300 GBP)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("27.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_hearing_min_range_FEE0222() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            300.01, "HearingSmallClaims")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0222");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (exceeds 300 but not 500 GBP)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("59.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_hearing_max_range_FEE0222() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            500, "HearingSmallClaims")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0222");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (exceeds 300 but not 500 GBP)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("59.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_hearing_boundary_FEE0222() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            555, "HearingFeeUpTo1500")
            .then().notFound();
    }

    @Test
    public void get_lookup_for_cmc_hearing_min_range_FEE0223() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            1000.01, "HearingSmallClaims")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0223");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (exceeds 1000 but not 1500 GBP)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("123.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_hearing_max_range_FEE0223() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            1500, "HearingSmallClaims")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0223");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (exceeds 1000 but not 1500 GBP)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("123.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_hearing_min_range_FEE0224() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            1500.01, "HearingSmallClaims")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0224");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (exceeds £1,500 but not £3,000)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("181.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_hearing_max_range_FEE0224() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            3000, "HearingSmallClaims")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0224");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (exceeds £1,500 but not £3,000)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("181.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_hearing_min_range_FEE0225() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            3000.01, "HearingSmallClaims")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0225");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (exceeds 3000 GBP)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("346.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_hearing_max_range_FEE0225() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
            "civil", "county court", "default", "hearing",
            10000, "HearingSmallClaims")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0225");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Hearing fee: Small claims case (exceeds 3000 GBP)");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("346.00");
        });
    }

    @Test
    public void get_lookup_for_cmc_no_range_FEE0001() throws IOException {

            scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAndKeywordUnspecifiedClaims("civil money claims",
                    "civil", "county court", "default", "issue", "MoneyClaim")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
                Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0001");
                Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Money Claims - Claim Amount - Unspecified");
                Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
                Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("10000.00");
            });
    }

    //Negative tests

    @Test
    public void negative_get_lookup_for_cmc_hearing_FEE0183_fee_too_low() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "hearing",
                0.01, "HearingSmallClaims")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_FEE0202_fee_too_high() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "issue",
                300.01, "MoneyClaim")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_FEE0203_fee_too_low() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "issue",
                300, "MoneyClaim")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_FEE0204_fee_too_high() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "issue",
                10000.01, "MoneyClaim")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_FEE0205_fee_too_low() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "issue",
                1000, "MoneyClaim")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_FEE0206_fee_too_high() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "issue",
                3000.01,"MoneyClaim")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_FEE0207_fee_too_low() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "issue",
                3000, "MoneyClaim")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_FEE0207_fee_too_high() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "issue",
                5000.01, "MoneyClaim")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_FEE0208_fee_too_high() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "issue",
                10000.01,"MoneyClaim")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_FEE0209_fee_too_low() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "issue",
                10000, "MoneyClaim")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_FEE0210_fee_too_low() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "issue",
                0.01, "MoneyClaim")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_hearing_FEE0221_fee_too_high() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "hearing",
                300.01, "HearingSmallClaims")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_hearing_FEE0222_fee_too_high() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "hearing",
                500.01, "HearingSmallClaims")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_hearing_FEE0223_fee_too_low() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "hearing",
                1000, "HearingSmallClaims")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_hearing_FEE0224_fee_too_high() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "hearing",
                3000.01, "HearingSmallClaims")
            .then().notFound();
    }

    @Test
    public void negative_get_lookup_for_cmc_hearing_FEE0225_fee_too_low() throws IOException {

        scenario.given()
            .when().getLookUpForCMCResponseWithMandatoryFieldsAmountAndKeyword("civil money claims",
                "civil", "county court", "default", "hearing",
                3000, "HearingSmallClaims")
            .then().notFound();
    }

    @Test
    public void getlookupresponseMessageForProbate() throws IOException {

        scenario.given()
            .when().getLookUpForProbateResponseWithKeywordApplicationType("probate", "family", "probate registry", "default", "issue", "all", parseDouble("5000.01"),"SA")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0219");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("273.00");
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
    public void getLookupResponseForProbateFeeWithMaxRangeAs5000() {

        scenario.given()
            .when().getLookUpResponseWithKeywordAmount("probate", "family", "probate registry", "default", "issue", "SAL5K", 5000)
            .then().ok().got(FeeLookupResponseDto.class, feeLookupResponseDto -> {
                Assertions.assertThat(feeLookupResponseDto.getDescription()).isEqualTo("Application for a grant of probate (Estate under 5000 GBP)");
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
            .when().getLookUpResponseWithKeywordAmount("probate", "family", "probate registry", "default", "issue", "SAL5K", 5000)
            .then().ok().got(FeeLookupResponseDto.class, feeLookupResponseDto -> {
            Assertions.assertThat(feeLookupResponseDto.getDescription()).isEqualTo("Application for a grant of probate (Estate under 5000 GBP)");
            Assertions.assertThat(feeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(feeLookupResponseDto.getFeeAmount()).isEqualTo("0.00");
        });
    }

    @Test
    public void getLookupResponseForProbateFeeWithKeywordSA() {

        scenario.given()
            .when().getLookUpResponseWithKeywordAmount("probate", "family", "probate registry", "default", "issue", "SA", 5000.01)
            .then().ok().got(FeeLookupResponseDto.class, feeLookupResponseDto -> {
            Assertions.assertThat(feeLookupResponseDto.getDescription()).isEqualTo("Application for a grant of probate (Estate over 5000 GBP)");
            Assertions.assertThat(feeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(feeLookupResponseDto.getFeeAmount()).isEqualTo("273.00");
        });
    }

    @Test
    public void getlookupresponseMessageForFPL() throws IOException {

        scenario.given()
            .when().getLookUpResponseWithKeyword("public law", "family", "family court", "default", "issue", "CareOrder")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0313");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Application for proceedings under Section 31 of Act");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("2215.00");
        });
    }
}
