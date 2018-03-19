package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.model.DirectionType;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.math.BigDecimal;

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

        CreateFixedFeeDto dto = new CreateFixedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");

        dto.setCode(String.valueOf(System.currentTimeMillis()));
        dto.setUnspecifiedClaimAmount(true);
        dto.setChannel("online");

        FeeVersionDto version = new FeeVersionDto();
        version.setDescription(version.getMemoLine());
        version.setDirection("licence");
        version.setMemoLine("description");
        version.setFlatAmount(new FlatAmountDto(new BigDecimal(10000)));

        dto.setVersion(version);

        saveFeeAndCheckStatusIsCreated(dto);

        getFeeAndExpectStatusIsOk(dto.getCode())
            .andExpect(versionIsOneAndStatusIsDraft())
        .andExpect(isUnspecifiedAmountFee());

        deleteFee(dto.getCode());

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

        CreateFixedFeeDto dto = new CreateFixedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");
        dto.setCode(String.valueOf(System.currentTimeMillis()));

        dto.setUnspecifiedClaimAmount(true);

        FeeVersionDto version = new FeeVersionDto();
        version.setDescription(version.getMemoLine());
        version.setDirection("licence");
        version.setMemoLine("description");
        version.setFlatAmount(new FlatAmountDto(new BigDecimal(10000)));

        dto.setVersion(version);

        saveFeeAndCheckStatusIsCreated(dto);

        getFeeAndExpectStatusIsOk(dto.getCode())
            .andExpect(versionIsOneAndStatusIsDraft())
            .andExpect(isUnspecifiedAmountFee());

        deleteFee(dto.getCode());

    }

    /* NOTE:****IT SHOULD NOT BE POSSIBLE TO HAVE THIS FEE IN % */

    @Test
    public synchronized void testCreateUnspecifiedAmountFeeForPercentAmounts() throws Exception{

        CreateFixedFeeDto dto = new CreateFixedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");
        dto.setCode(String.valueOf(System.currentTimeMillis()));
        dto.setUnspecifiedClaimAmount(true);

        FeeVersionDto version = new FeeVersionDto();
        version.setMemoLine("description");
        version.setDescription(version.getMemoLine());
        version.setPercentageAmount(new PercentageAmountDto(new BigDecimal("0.01")));

        dto.setVersion(version);

        saveFee(dto).andExpect(status().isBadRequest());

        deleteFee(dto.getCode());
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

        CreateFixedFeeDto dto = new CreateFixedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");
        dto.setChannel("online");
        dto.setApplicant("all");
        dto.setCode(String.valueOf(System.currentTimeMillis()));
        dto.setUnspecifiedClaimAmount(true);

        FeeVersionDto version = new FeeVersionDto();
        version.setMemoLine("description");
        version.setDescription(version.getMemoLine());
        version.setDirection("licence");
        version.setStatus(FeeVersionStatus.approved);
        version.setFlatAmount(new FlatAmountDto(new BigDecimal(10000)));

        dto.setVersion(version);

        saveFeeAndCheckStatusIsCreated(dto);

        lookupUsingUsingReferenceDataFrom(dto, null)
            .andExpect(lookupResultMatchesFee(dto))
            .andExpect(lookupResultMatchesExpectedFeeAmount(version.getFlatAmount().getAmount()));

        deleteFee(dto.getCode());

    }


}
