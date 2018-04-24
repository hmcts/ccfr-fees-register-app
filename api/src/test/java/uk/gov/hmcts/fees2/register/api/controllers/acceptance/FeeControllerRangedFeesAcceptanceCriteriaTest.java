package uk.gov.hmcts.fees2.register.api.controllers.acceptance;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;

import java.math.BigDecimal;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeeControllerRangedFeesAcceptanceCriteriaTest extends BaseIntegrationTest {

    /* PAY-449 */

    /* Scenario 1: Creating a % fee for the ONLINE channel

    Given I want to create a ranged fee for "Civil Money Claims" for an "issue" event
    And I have specified Jurisdiction1 as "civil"
    And I have specified Jurisdiction2 as "county court"
    And I have specified a minimum range
    And I have specified a maximum range
    And I have specified "online" channel
    And I have specified the fee code
    And I have specified the fee description
    And I have specified the date valid FROM & TO
    And the fee amount is a "% amount"
    And I enter the flat amount is defined
    When I save this fee
    Then the fee is SAVED successfully
    And the fee status is Draft
    And the fee version is 1
    */

    @Test
    public synchronized void testCreateRangedFeeWithPercentAmount() throws Exception {

        CreateRangedFeeDto dto = createCMCIssueCivilCountyRangedFee();
        dto.setChannel("online");

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setPercentageAmount(new PercentageAmountDto(new BigDecimal("10")));
        versionDto.setDescription(versionDto.getMemoLine());
        versionDto.setValidFrom(new Date());
        versionDto.setValidTo(new Date(System.currentTimeMillis() + 360000));

        dto.setVersion(versionDto);

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        getFeeAndExpectStatusIsOk(uri[3])
            .andExpect(versionIsOneAndStatusIsDraft());

        deleteFee(uri[3]);
    }

    /* Scenario 2: Creating a % fee for the DEFAULT channel

    Given I want to create a ranged fee for "Civil Money Claims" for an "issue" event
    And I have specified Jurisdiction1 as "civil"
    And I have specified Jurisdiction2 as "county court"
    And I have specified a minimum range
    And I have specified a maximum range
    And I have specified "default" channel
    And I have specified the fee code
    And I have specified the fee description
    And I have specified the date valid FROM & TO
    And the fee amount is a "% amount"
    And I enter the flat amount is defined
    When I save this fee
    Then the fee is SAVED successfully
    And the fee status is Draft
    And the fee version is 1
    */

    @Test
    public synchronized void testCreateRangedFeeWithPercentAmountAndDefaultChannel() throws Exception {

        CreateRangedFeeDto dto = createCMCIssueCivilCountyRangedFee();

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setPercentageAmount(new PercentageAmountDto(new BigDecimal("10")));
        versionDto.setDescription(versionDto.getMemoLine());
        versionDto.setValidFrom(new Date());
        versionDto.setValidTo(new Date(System.currentTimeMillis() + 360000));

        dto.setVersion(versionDto);

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        getFeeAndExpectStatusIsOk(uri[3])
            .andExpect(channelIsDefault())
            .andExpect(versionIsOneAndStatusIsDraft());

        deleteFee(uri[3]);
    }

    /* PAY-444 */

    /* Scenario 1: Creating a FLAT fee for the ONLINE channel
       ------------------------------------------------------

    Given I want to create a ranged fee for "Civil Money Claims" for an "issue" event
    And I have specified Jurisdiction1 as "civil"
    And I have specified Jurisdiction2 as "county court"
    And I have specified a minimum range
    And I have specified a maximum range
    And I have specified "online" channel
    And I have specified the fee codeAnd I have specified the fee description
    And the fee amount is a "flat amount"
    And  the flat amount is defined
    When I save this fee
    Then the fee is SAVED successfully
    And the fee status is Draft
    And the fee version is 1

    */

    @Test
    public synchronized void testCreateRangedFeeWithFlatAmount() throws Exception {

        CreateRangedFeeDto dto = createCMCIssueCivilCountyRangedFee();
        dto.setChannel("online");

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(versionDto.getMemoLine());

        dto.setVersion(versionDto);

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        getFeeAndExpectStatusIsOk(uri[3])
            .andExpect(versionIsOneAndStatusIsDraft());

        deleteFee(uri[3]);
    }

    /* Scenario 2: Creating a FLAT fee for the DEFAULT channel
       -------------------------------------------------------

    Given I want to create a ranged fee for "Civil Money Claims" for an "issue" event
    And I have specified Jurisdiction1 as "civil"
    And I have specified Jurisdiction2 as "county court"
    And I have specified a minimum range
    And I have specified a maximum range
    And I have specified "default" channel
    And I have specified the fee codeAnd I have specified the fee description
    And the fee amount is a "flat amount"
    And I enter the flat amount is defined
    When I save this fee
    Then the fee is SAVED successfully
    And the fee status is Draft
    And the fee version is 1
    */

    @Test
    public synchronized void testCreateRangedFeeWithFlatAmountAndDefaultChannel() throws Exception {

        CreateRangedFeeDto dto = createCMCIssueCivilCountyRangedFee();

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(versionDto.getMemoLine());

        dto.setVersion(versionDto);

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        getFeeAndExpectStatusIsOk(uri[3])
            .andExpect(versionIsOneAndStatusIsDraft())
            .andExpect(channelIsDefault());

        deleteFee(uri[3]);
    }

    private CreateRangedFeeDto createCMCIssueCivilCountyRangedFee() {
        CreateRangedFeeDto dto = new CreateRangedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");
        dto.setMinRange(BigDecimal.ONE);
        dto.setMaxRange(BigDecimal.TEN);

        return dto;
    }

    /* PAY-476: Check ranges are valid */

    @Test
    public synchronized void testInvalidRange() throws Exception {

        CreateRangedFeeDto dto = createCMCIssueCivilCountyRangedFee();
        dto.setMaxRange(BigDecimal.ZERO);
        dto.setMinRange(BigDecimal.TEN);

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(versionDto.getMemoLine());

        dto.setVersion(versionDto);

        saveFee(dto).andExpect(status().isBadRequest());
    }

    @Test
    public synchronized void testInvalidRangeSameRange() throws Exception {

        CreateRangedFeeDto dto = createCMCIssueCivilCountyRangedFee();
        dto.setMaxRange(BigDecimal.TEN);
        dto.setMinRange(BigDecimal.TEN);

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(versionDto.getMemoLine());

        dto.setVersion(versionDto);

        saveFee(dto).andExpect(status().isBadRequest());
    }

}
