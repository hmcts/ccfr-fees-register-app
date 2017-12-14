package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LookupFeeAcceptanceCriteriaTest extends BaseIntegrationTest{

    /* --- PAY-440 --- */

    /*Scenario 1: Looking up a FLAT fee with ONLINE channel

    Given a "citizen" wants to "issue" a claim using the "civil money claims" service
    And the claim will be in the "county court"
    And the claim will be for a "civil" jurisdiction
    And the claim value is known
    And the claim is made through "online" channel
    When the fee for this claim is being looked up through the API
    And the fee amount is a "flat amount"
    And there exists an "Approved" version of the fee which is valid as of today
    Then the API response will have the FeeCode, FeeVersion, FeeDescription and the FeeAmount
    */

    @Test
    public synchronized void testLookupFlatFeeAmount() throws Exception{


        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createCMCIssueCivilCountyFixedFee();
        dto.setChannel("online");

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(dto.getMemoLine());
        versionDto.setStatus(FeeVersionStatus.approved);

        dto.setVersion(versionDto);

        saveFeeAndCheckStatusIsCreated(dto);

        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isOk())
            .andExpect(lookupResultMatchesFee(dto))
            .andExpect(lookupResultMatchesExpectedFeeAmount(versionDto.getFlatAmount().getAmount()));

        deleteFee(dto.getCode());
    }

    /* Scenario 2: Looking up a % fee with ONLINE channel

    Given a "citizen" wants to "issue" a claim using the "civil money claims" service
    And the claim will be in the "county court"
    And the claim will be for a "civil" jurisdiction
    And the claim value is known
    And the claim is made through "online" channel
    When the fee for this claim is being looked up through the API
    And the fee amount is a "% amount"
    And there exists an "Approved" version of the fee which is valid as of today
    Then the API response will have the FeeCode, FeeVersion, FeeDescription and the FeeAmount

    */

    @Test
    public synchronized void testLookupPercentFeeAmount() throws Exception{

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createCMCIssueCivilCountyFixedFee();
        dto.setChannel("online");

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setPercentageAmount(new PercentageAmountDto(new BigDecimal(10)));
        versionDto.setDescription(dto.getMemoLine());
        versionDto.setStatus(FeeVersionStatus.approved);
        dto.setVersion(versionDto);

        saveFeeAndCheckStatusIsCreated(dto);

        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isOk())
            .andExpect(lookupResultMatchesFee(dto))
            .andExpect(lookupResultMatchesExpectedFeeAmount(new BigDecimal("0.5")));

        deleteFee(dto.getCode());
    }

    /* Scenario 3: Looking up a FLAT fee with DEFAULT channel

    Given a "citizen" wants to "issue" a claim using the "civil money claims" service
    And the claim will be in the "county court"
    And the claim will be for a "civil" jurisdiction
    And the claim value is known
    And the claim is made through "default" channel
    When the fee for this claim is being looked up through the API
    And the fee amount is a "flat amount"
    And there exists an "Approved" version of the fee which is valid as of today
    Then the API response will have the FeeCode, FeeVersion, FeeDescription and the FeeAmount
    */

    @Test
    public synchronized void testLookupFlatFeeAmountWithDefaultChannel() throws Exception{

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createCMCIssueCivilCountyFixedFee();

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(dto.getMemoLine());
        versionDto.setStatus(FeeVersionStatus.approved);
        dto.setVersion(versionDto);

        saveFeeAndCheckStatusIsCreated(dto);

        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isOk())
            .andExpect(lookupResultMatchesFee(dto))
            .andExpect(lookupResultMatchesExpectedFeeAmount(versionDto.getFlatAmount().getAmount()));

        deleteFee(dto.getCode());
    }

    /* Scenario 4: Looking up a % fee with DEFAULT channel

    Given a "citizen" wants to "issue" a claim using the "civil money claims" service
    And the claim will be in the "county court"
    And the claim will be for a "civil" jurisdiction
    And the claim value is known
    And the claim is made through "default" channel
    When the fee for this claim is being looked up through the API
    And the fee amount is a "% amount"
    And there exists an "Approved" version of the fee which is valid as of today
    Then the API response will have the FeeCode, FeeVersion, FeeDescription and the FeeAmount

    */

    @Test
    public synchronized void testLookupPercentFeeAmountWithDefaultChannel() throws Exception{

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createCMCIssueCivilCountyFixedFee();

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setPercentageAmount(new PercentageAmountDto(new BigDecimal(10))); // 10%
        versionDto.setDescription(dto.getMemoLine());
        versionDto.setStatus(FeeVersionStatus.approved);
        dto.setVersion(versionDto);

        saveFeeAndCheckStatusIsCreated(dto);

        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isOk())
            .andExpect(lookupResultMatchesFee(dto))
            .andExpect(lookupResultMatchesExpectedFeeAmount(new BigDecimal("0.5")));

        deleteFee(dto.getCode());
    }

    /* Scenario 5: Looking up fees where no approved fee exists

    Given a "citizen" wants to "issue" a claim using the "civil money claims" service
    And the claim will be in the "county court"
    And the claim will be for a "civil" jurisdiction
    And the claim value is known
    And the claim is made through "default" OR ''online'' channel
    When the fee for this claim is being looked up through the API
    And the fee amount is a "% amount" OR ''flat amount''
    And there is NO "Approved" version of the fee which is valid as of today
    Then the API response will return an error

    */

    @Test
    public synchronized void testLookupFeeWhereNoApproved() throws Exception{

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createCMCIssueCivilCountyFixedFee();

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(dto.getMemoLine());
        dto.setVersion(versionDto);

        saveFeeAndCheckStatusIsCreated(dto);

        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isNotFound());

        deleteFee(dto.getCode());
    }

    /* --- PAY-457 --- */

    /* Scenario 1: Looking up a FLAT fee with DEFAULT channel

    Given a "citizen" wants to apply for a divorce using the "Divorce" service
    And the case will be IN the "family" jurisdiction
    And the case will be FOR a "family" jurisdiction
    And the case is made through "default" channel
    When the fee for this case is being looked up through the API
    And the fee amount is a "flat amount"
    And there exists an "Approved" version of the fee which is valid as of today
    Then the API response will have the FeeCode, FeeVersion, FeeDescription and the FeeAmount

    */

    @Test
    public synchronized void testLookupFlatFeeWithDefaultChannelSpecified() throws Exception{

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createDivorceIssueFamilyFixedFee();
        dto.setChannel("online");

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(dto.getMemoLine());
        versionDto.setStatus(FeeVersionStatus.approved);

        dto.setVersion(versionDto);

        saveFeeAndCheckStatusIsCreated(dto);

        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isOk())
            .andExpect(lookupResultMatchesFee(dto))
            .andExpect(lookupResultMatchesExpectedFeeAmount(versionDto.getFlatAmount().getAmount()));

        deleteFee(dto.getCode());

    }

    /* Scenario 2: Looking up fees where no approved fee exists

    Given a "citizen" wants to apply for a divorce using the "Divorce" service
    And the case will be IN the "family" jurisdiction
    And the case will be FOR a "family" jurisdiction
    And the case is made through "default" channel
    When the fee for this case is being looked up through the API
    And the fee amount is a "flat amount"
    And there is NO "Approved" version of the fee which is valid as of today
    Then the API response will return an error

    */

    @Test
    public synchronized void testLookupFeeWhereNoApprovedDivorceDefaultChannel() throws Exception{

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createDivorceIssueFamilyFixedFee();
        dto.setChannel("online");

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(dto.getMemoLine());
        dto.setVersion(versionDto);

        saveFeeAndCheckStatusIsCreated(dto);

        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isNotFound());

        deleteFee(dto.getCode());
    }

    /* -- PAY-4487 -- */

    /* Scenario1: Solicitors application for estates valued above £5k */

    /* Given Application is already deployed in prod with old fees register data in there And new needed ranged fee for "probate" for an "issue" event details are
    Jurisdiction1 as "family"
    And Jurisdiction2 as "probate"
    And a minimum range of £5000.00
    And  a maximum range
    And "default" channel
    And the fee code
    And have specified the fee description
    And the fee amount is a "flat amount"
    And the flat amount is defined as £155
    And a ''valid from'' and ''valid to'' date
    And  SI Ref data
    And fee order name
    And statutory instruments
    And Natural Account Code
    And the memoline
    When application is started
    Then the fee is SAVED successfully
    And the fee status is Approved
    And the fee version is 1
    */

    @Test
    public synchronized void testLookupProbateFeeForEstateValuedMoreThan5000ReturnsASingleResult() throws Exception{

        /* We want to make sure the fee will be in place for probate in a functional state and not collisioning with similar fees */

        LookupFeeDto lookup = new LookupFeeDto();
        lookup.setAmount(new BigDecimal(10000));
        lookup.setService("probate");
        lookup.setJurisdiction1("family");
        lookup.setJurisdiction2("probate registry");
        lookup.setChannel("default");
        lookup.setEvent("issue");

        lookup(lookup).andExpect(status().isOk());
    }
}
