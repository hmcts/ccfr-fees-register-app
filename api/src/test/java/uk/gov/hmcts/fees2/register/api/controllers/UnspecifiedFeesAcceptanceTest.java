package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UnspecifiedFeesAcceptanceTest extends BaseIntegrationTest {

    /*   --- PAY-454 --- */

    /*
    Scenario 1: Creating a FLAT fee for the ONLINE channel

    Given I want to create a fee for "Civil Money Claims" for an "issue" event
    And I have specified Jurisdiction1 as "civil"
    And I have specified Jurisdiction2 as "county court"
    And I have indicated that this fee is for unspecified claims
    And I have specified "online" channel
    And I have specified the fee codeAnd I have specified the fee description
    And the fee amount is £10,000
    When I save this fee
    Then the fee is SAVED successfully
    And the fee status is Draft
    And the fee version is 1
    */

    @Test
    public synchronized void testCreateUnspecifiedAmountFeeForOnlineChannel() throws Exception{

        FixedFeeDto dto = new FixedFeeDto();
        dto.setService("gambling");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("magistrates court");
        dto.setApplicantType("personal");
        dto.setUnspecifiedClaimAmount(true);
        dto.setChannel("online");

        FeeVersionDto version = new FeeVersionDto();
        version.setDescription(version.getMemoLine());
        version.setDirection("enhanced");
        version.setMemoLine("description");
        version.setFlatAmount(new FlatAmountDto(new BigDecimal(10000)));

        dto.setVersion(version);

        String loc = restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", dto)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");

        restActions
            .get("/fees-register/fees/" + arr[3])
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, fee -> {
                assertThat(fee.getCode()).isEqualTo(arr[3]);
                assertThat(fee.getServiceTypeDto().getName()).isEqualTo("gambling");
                assertThat(fee.getJurisdiction1Dto().getName()).isEqualTo("civil");
                assertThat(fee.getJurisdiction2Dto().getName()).isEqualTo("magistrates court");
                assertThat(fee.getChannelTypeDto().getName()).isEqualTo("online");
                fee.getFeeVersionDtos().stream().forEach(v -> {
                    assertThat(v.getStatus()).isEqualTo(FeeVersionStatus.draft);
                    assertThat(v.getFlatAmount().getAmount()).isEqualTo("10000.00");
                });
            }));

        forceDeleteFee(arr[3]);
    }

    /* Scenario 2: Creating a FLAT fee for the DEFAULT channel

    Given I want to create a  fee for "Civil Money Claims" for an "issue" event
    And I have specified Jurisdiction1 as "civil"
    And I have specified Jurisdiction2 as "county court"
    And I have indicated that this fee is for unspecified claims
    And I have specified "online" channel
    And I have specified the fee codeAnd I have specified the fee description
    And the fee amount is £10,000
    When I save this fee
    Then the fee is SAVED successfully
    And the fee status is Draft
    And the fee version is 1
    */
    @Test
    public synchronized void testCreateUnspecifiedAmountFeeForDefaultChannel() throws Exception{

        FixedFeeDto dto = new FixedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");

        dto.setUnspecifiedClaimAmount(true);

        FeeVersionDto version = new FeeVersionDto();
        version.setDescription(version.getMemoLine());
        version.setDirection("licence");
        version.setMemoLine("description");
        version.setFlatAmount(new FlatAmountDto(new BigDecimal(10000)));

        dto.setVersion(version);

        String loc = restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", dto)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");

        dto.setCode(arr[3]);
        System.out.println("Fee code: " + dto.getCode());

        restActions
            .get("/fees-register/fees/" + dto.getCode())
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, fee2Dto -> {
                assertThat(fee2Dto.getCode()).isEqualTo(arr[3]);
                assertThat(fee2Dto.isUnspecifiedClaimAmount()).isEqualTo(true);
                fee2Dto.getFeeVersionDtos().stream().forEach(v -> {
                    assertThat(v.getStatus()).isEqualTo(FeeVersionStatus.draft);
                    assertThat(v.getFlatAmount().getAmount()).isEqualTo(new BigDecimal("10000.00"));
                });
            }));

        forceDeleteFee(arr[3]);
    }

    /* NOTE:****IT SHOULD NOT BE POSSIBLE TO HAVE THIS FEE IN % */

    @Test
    public synchronized void testCreateUnspecifiedAmountFeeForPercentAmounts() throws Exception{

        FixedFeeDto dto = new FixedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");
        dto.setUnspecifiedClaimAmount(true);

        FeeVersionDto version = new FeeVersionDto();
        version.setMemoLine("description");
        version.setDescription(version.getMemoLine());
        version.setPercentageAmount(new PercentageAmountDto(new BigDecimal("0.01")));

        dto.setVersion(version);

        saveFee(dto).andExpect(status().isBadRequest());
    }

    /* - PAY-447 */

    /* Scenario 2: Claim-range UNknown, claim amount UNknown

    GIVEN that a cmc claim is being submitted
    And the claim will be in the "county court"
    And the claim will be for a "civil" jurisdiction
    And the fee register claim range is UNknown
    And the claim value is UNKNOWN
    And the claim is made through "online" or ‘’default’’ channel
    And the fee for this claim is being looked up through the API
    WHEN the claim value is unspecified WITH NO claim-amount
    THEN return the MAXIMUM fee value for unspecified claims together with FeeCode, FeeVersion, FeeDescription
    */

    @Test
    public synchronized void testThatLookupOfUnspecifiedClaimAmountMatchesUnspecifiedTypeFee() throws Exception{

        FixedFeeDto dto = new FixedFeeDto();
        dto.setService("general");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");
        dto.setChannel("online");
        dto.setApplicantType("all");
        dto.setUnspecifiedClaimAmount(true);

        FeeVersionDto version = new FeeVersionDto();
        version.setMemoLine("description");
        version.setDescription(version.getMemoLine());
        version.setDirection("licence");
        version.setStatus(FeeVersionStatus.approved);
        version.setFlatAmount(new FlatAmountDto(new BigDecimal(25000)));

        dto.setVersion(version);

        String loc = restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", dto)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");

        restActions
            .get("/fees-register/fees/lookup-unspecified?service=general&jurisdiction1=civil&jurisdiction2=county court&channel=online&event=issue&applicant_type=all")
            .andExpect(status().isOk())
            .andExpect(body().as(FeeLookupResponseDto.class, fee -> {
                assertThat(fee.getCode()).isEqualTo(arr[3]);
                assertThat(fee.getDescription()).isEqualTo(version.getDescription());
                assertThat(fee.getFeeAmount()).isEqualTo(new BigDecimal("25000.00"));
            }));
        forceDeleteFee(arr[3]);
    }


}
