package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.util.URIUtils;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeeControllerRangedFeesAcceptanceCriteriaTest extends BaseTest {

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
    public void testCreateRangedFeeWithPercentAmount() throws Exception {

        CreateRangedFeeDto dto = new CreateRangedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");
        dto.setMinRange(BigDecimal.ONE);
        dto.setMaxRange(BigDecimal.TEN);
        dto.setChannel("online");
        dto.setCode(String.valueOf(System.currentTimeMillis()));
        dto.setMemoLine("description");

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setPercentageAmount(new PercentageAmountDto(new BigDecimal("0.1")));
        versionDto.setDescription(dto.getMemoLine());
        versionDto.setValidFrom(new Date());
        versionDto.setValidTo(new Date(System.currentTimeMillis() + 360000));

        dto.setVersion(versionDto);

        saveRangedFeeAndCheckStatusIsCreated(dto);

        getFeeAndExpectStatusIsOk(dto.getCode())
            .andExpect(versionIsOneAndStatusIsDraft());

    }

    @Test
    public void testCreateRangedFeeWithPercentAmountAndDefaultChannel() throws Exception {

        CreateRangedFeeDto dto = new CreateRangedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");
        dto.setMinRange(BigDecimal.ONE);
        dto.setMaxRange(BigDecimal.TEN);
        dto.setCode(String.valueOf(System.currentTimeMillis()));
        dto.setMemoLine("description");

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setPercentageAmount(new PercentageAmountDto(new BigDecimal("0.1")));
        versionDto.setDescription(dto.getMemoLine());
        versionDto.setValidFrom(new Date());
        versionDto.setValidTo(new Date(System.currentTimeMillis() + 360000));

        dto.setVersion(versionDto);

        saveRangedFeeAndCheckStatusIsCreated(dto);

        getFeeAndExpectStatusIsOk(dto.getCode())
            .andExpect(channelIsDefault())
            .andExpect(versionIsOneAndStatusIsDraft());

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
    public void testCreateRangedFeeWithFlatAmount() throws Exception {

        CreateRangedFeeDto dto = new CreateRangedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");
        dto.setMinRange(BigDecimal.ONE);
        dto.setMaxRange(BigDecimal.TEN);
        dto.setChannel("online");
        dto.setCode(String.valueOf(System.currentTimeMillis()));
        dto.setMemoLine("description");

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(dto.getMemoLine());

        dto.setVersion(versionDto);

        saveRangedFeeAndCheckStatusIsCreated(dto);

        getFeeAndExpectStatusIsOk(dto.getCode())
            .andExpect(versionIsOneAndStatusIsDraft());
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
    public void testCreateRangedFeeWithFlatAmountAndDefaultChannel() throws Exception {

        CreateRangedFeeDto dto = new CreateRangedFeeDto();
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("county court");
        dto.setMinRange(BigDecimal.ONE);
        dto.setMaxRange(BigDecimal.TEN);
        dto.setCode(String.valueOf(System.currentTimeMillis()));
        dto.setMemoLine("description");

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(dto.getMemoLine());

        dto.setVersion(versionDto);

        saveRangedFeeAndCheckStatusIsCreated(dto);

        getFeeAndExpectStatusIsOk(dto.getCode())
            .andExpect(versionIsOneAndStatusIsDraft())
            .andExpect(channelIsDefault());
    }

    /* -- Low Level -- */

    private ResultActions getFeeAndExpectStatusIsOk(String code) throws Exception {
        return restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "getFee"), code)
            .andExpect(status().isOk());
    }

    private void saveRangedFeeAndCheckStatusIsCreated(CreateRangedFeeDto dto) throws Exception {
        restActions
            .withUser("admin")
            .post(
                URIUtils.getUrlForPostMethod(FeeController.class, "createRangedFee"),
                dto
            )
            .andExpect(status().isCreated());
    }

    private ResultMatcher versionIsOneAndStatusIsDraft() {
        return body().as(Fee2Dto.class, (feeDto) -> {

            FeeVersionDto v = feeDto.getFeeVersionDtos().get(0);
            assertThat(v.getVersion().equals(1));
            assertThat(v.getStatus() == FeeVersionStatus.draft);
        });
    }

    private ResultMatcher channelIsDefault() {
        return body().as(Fee2Dto.class, (feeDto) -> {
            assertThat(feeDto.getChannelTypeDto().getName().equals(ChannelType.DEFAULT));
        });
    }

}
