package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;

import java.math.BigDecimal;

public class FeeControllerFixedFeesAcceptanceCriteriaTest extends BaseIntegrationTest {

    /* PAY-455 */

    /* ACCEPTANCE CRITERIA

    Given I want to create a fixed fee for "Divorce" for an "issue" event
    And I have specified Jurisdiction1 as "family"
    And I have specified Jurisdiction2 as "family"
    And I have specified "default" channel
    And I have specified the fee codeAnd I have specified the fee description
    And the fee amount is a "flat amount"
    And  the flat amount is defined
    When I save this fee
    Then the fee is SAVED successfully
    And the fee status is Draft
    And the fee version is 1

*/

    @Test
    public synchronized void testFixedFee() throws Exception{

        CreateFixedFeeDto dto = new CreateFixedFeeDto();
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("family");
        dto.setJurisdiction2("high court");
        dto.setCode(String.valueOf(System.currentTimeMillis()));
        dto.setMemoLine("description");

        FeeVersionDto version = new FeeVersionDto();
        version.setDescription(dto.getMemoLine());
        version.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));

        dto.setVersion(version);

        saveFeeAndCheckStatusIsCreated(dto);

        getFeeAndExpectStatusIsOk(dto.getCode())
            .andExpect(versionIsOneAndStatusIsDraft());

        deleteFee(dto.getCode());

    }

}
