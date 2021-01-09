package uk.gov.hmcts.fees.register.functional;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees.register.functional.dsl.FeesRegisterTestDsl;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;

import java.io.IOException;
import java.math.BigDecimal;

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


    @Test
    public void getLookupResponseForProbateFeeWithMaxRangeAs5000() {

        scenario.given()
            .when().getLookUpForProbateResponse("probate", "family", "probate registry", "default", "issue", "personal", new BigDecimal("5000"))
            .then().ok().got(FeeLookupResponseDto.class, feeLookupResponseDto -> {
                Assertions.assertThat(feeLookupResponseDto.getDescription()).isEqualTo("Personal Application for grant of Probate");
                Assertions.assertThat(feeLookupResponseDto.getVersion()).isNotNull();
                Assertions.assertThat(feeLookupResponseDto.getFeeAmount()).isEqualTo(new BigDecimal("0.00"));
        });
    }

    @Test
    public void getLookupResponseForProbateFeeWithvolumeAmount() {

        scenario.given()
            .when().getLookUpResponsewithkeywordvolumeamount("probate", "family", "probate registry", "default", "copies", "NewFee", 2, "all")
            .then().ok().got(FeeLookupResponseDto.class, feeLookupResponseDto -> {
            Assertions.assertThat(feeLookupResponseDto.getDescription()).isEqualTo("Copy of a document (for each copy)");
            Assertions.assertThat(feeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(feeLookupResponseDto.getFeeAmount()).isEqualTo(new BigDecimal("3"));
        });
    }

    @Test
    public void getlookupresponseMessageForFPL() throws IOException {

        scenario.given()
            .when().getLookUpResponsewithkeyword("public law", "family", "family court", "default", "issue", "CareOrder")
            .then().ok().got(FeeLookupResponseDto.class, FeeLookupResponseDto -> {
            Assertions.assertThat(FeeLookupResponseDto.getCode()).isEqualTo("FEE0314");
            Assertions.assertThat(FeeLookupResponseDto.getDescription()).isEqualTo("Application for proceedings under Section 31 of Act");
            Assertions.assertThat(FeeLookupResponseDto.getVersion()).isNotNull();
            Assertions.assertThat(FeeLookupResponseDto.getFeeAmount()).isEqualTo("2055.00");
        });
    }
}
