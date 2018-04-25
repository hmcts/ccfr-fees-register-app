package uk.gov.hmcts.fees2.register.api.controllers.acceptance;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.math.BigDecimal;
import java.math.BigInteger;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LookupFeeAcceptanceCriteriaTest extends BaseIntegrationTest {

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
    public synchronized void testLookupFlatFeeAmount() throws Exception {


        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createCMCIssueCivilCountyFixedFee();
        dto.setChannel("online");
        dto.setApplicantType("all");

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(versionDto.getMemoLine());
        versionDto.setStatus(FeeVersionStatus.approved);

        dto.setVersion(versionDto);

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        dto.setCode(uri[3]);
        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isOk())
            .andExpect(lookupResultMatchesFee(dto))
            .andExpect(lookupResultMatchesExpectedFeeAmount(versionDto.getFlatAmount().getAmount()));

        forceDeleteFee(uri[3]);
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
    public synchronized void testLookupPercentFeeAmount() throws Exception {

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createCMCIssueCivilCountyFixedFee();
        dto.setChannel("online");
        dto.setApplicantType("all");

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setPercentageAmount(new PercentageAmountDto(new BigDecimal(10)));
        versionDto.setDescription(versionDto.getMemoLine());
        versionDto.setStatus(FeeVersionStatus.approved);
        dto.setVersion(versionDto);

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        dto.setCode(uri[3]);
        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isOk())
            .andExpect(lookupResultMatchesFee(dto))
            .andExpect(lookupResultMatchesExpectedFeeAmount(new BigDecimal("0.5")));

        forceDeleteFee(uri[3]);
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
    public synchronized void testLookupFlatFeeAmountWithDefaultChannel() throws Exception {

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createCMCIssueCivilCountyFixedFee();

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(versionDto.getMemoLine());
        versionDto.setStatus(FeeVersionStatus.approved);
        dto.setVersion(versionDto);
        dto.setChannel("default");
        dto.setApplicantType("all");
        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        dto.setCode(uri[3]);
        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isOk())
            .andExpect(lookupResultMatchesFee(dto))
            .andExpect(lookupResultMatchesExpectedFeeAmount(versionDto.getFlatAmount().getAmount()));

        forceDeleteFee(uri[3]);
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
    public synchronized void testLookupPercentFeeAmountWithDefaultChannel() throws Exception {

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createCMCIssueCivilCountyFixedFee();

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setPercentageAmount(new PercentageAmountDto(new BigDecimal(10))); // 10%
        versionDto.setDescription(versionDto.getMemoLine());
        versionDto.setStatus(FeeVersionStatus.approved);
        dto.setVersion(versionDto);
        dto.setChannel("default");
        dto.setApplicantType("all");

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        dto.setCode(uri[3]);
        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isOk())
            .andExpect(lookupResultMatchesFee(dto))
            .andExpect(lookupResultMatchesExpectedFeeAmount(new BigDecimal("0.5")));

        forceDeleteFee(uri[3]);
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
    public synchronized void testLookupFeeWhereNoApproved() throws Exception {

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createCMCIssueCivilCountyFixedFee();

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(versionDto.getMemoLine());
        dto.setVersion(versionDto);
        dto.setChannel("default");
        dto.setApplicantType("all");

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        dto.setCode(uri[3]);
        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isNotFound());

        forceDeleteFee(uri[3]);
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
    public synchronized void testLookupFlatFeeWithDefaultChannelSpecified() throws Exception {

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createDivorceIssueFamilyFixedFee();
        dto.setChannel("online");
        dto.setApplicantType("all");

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(versionDto.getMemoLine());
        versionDto.setStatus(FeeVersionStatus.approved);

        dto.setVersion(versionDto);

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        dto.setCode(uri[3]);
        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isOk())
            .andExpect(lookupResultMatchesFee(dto))
            .andExpect(lookupResultMatchesExpectedFeeAmount(versionDto.getFlatAmount().getAmount()));

        forceDeleteFee(uri[3]);
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
    public synchronized void testLookupFeeWhereNoApprovedDivorceDefaultChannel() throws Exception {

        BigDecimal claimValue = new BigDecimal(5);

        CreateFixedFeeDto dto = createDivorceIssueFamilyFixedFee();
        dto.setChannel("online");

        FeeVersionDto versionDto = new FeeVersionDto();

        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription(versionDto.getMemoLine());
        dto.setVersion(versionDto);

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        dto.setCode(uri[3]);
        lookupUsingUsingReferenceDataFrom(dto, claimValue)
            .andExpect(status().isNotFound());

        forceDeleteFee(uri[3]);
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

    //@Test
    public void testLookupProbateFeeForEstateValuedMoreThan5000ReturnsASingleResult() throws Exception {

        /* We want to make sure the fee will be in place for probate in a functional state and not collisioning with similar fees */

        LookupFeeDto lookup = new LookupFeeDto();
        lookup.setAmountOrVolume(new BigDecimal("10000.00"));
        lookup.setService("probate");
        lookup.setJurisdiction1("family");
        lookup.setJurisdiction2("probate registry");
        lookup.setChannel("default");
        lookup.setEvent("issue");
        lookup.setApplicantType("all");

        lookup(lookup).andExpect(status().isOk());
    }

    /* - PAY-522 - */
    /*

    ACCEPTANCE CRITERIA

    Scenario1: Copies - fixed flat fee

    Given I want to create a fixed flat fee for "any given service" for a "copies" event
    And I have specified Jurisdiction1
    And I have specified Jurisdiction2
    And I have specified the channel
    And i have specified a ''valid from'' and ''valid to'' date
    And I have specified the fee code
    And I have specified the fee description
    And i have specified SI Ref data
    And i have specified fee order name
    And i have specified statutory instruments
    And i have specified Natural Account Code
    And i have specified the memoline
    And the fee amount is a "flat amount"
    When I save this fee
    Then the fee is SAVED successfully
    And the fee status is Draft
    And the fee version is 1
*/

    //@Test
    public void testLookupForProbateCopiesFeeX0258ReturnsCorrectResult() throws Exception{

        /* 1. Get the fee and get the amount per copy */

        getFeeAndExpectStatusIsOk("FEE0003").andExpect(
            body().as(Fee2Dto.class, (fee) -> {

                /* 2. Lookup the fee for 10 copies and test value is correct */

                LookupFeeDto lookupDto = new LookupFeeDto();
                lookupDto.setJurisdiction1(fee.getJurisdiction1Dto().getName());
                lookupDto.setJurisdiction2(fee.getJurisdiction2Dto().getName());
                lookupDto.setChannel(fee.getChannelTypeDto().getName());
                lookupDto.setApplicantType(fee.getApplicantTypeDto().getName());
                lookupDto.setEvent(fee.getEventTypeDto().getName());
                lookupDto.setService(fee.getServiceTypeDto().getName());
                lookupDto.setAmountOrVolume(BigDecimal.TEN);

                try {
                    lookup(lookupDto)
                        .andExpect(lookupResultMatchesExpectedFeeAmount(lookupDto.getAmountOrVolume().multiply(fee.getCurrentVersion().getVolumeAmount().getAmount())));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            })
        );
    }

    /* PAY - 527 */

    /* Scenario1: Application for estates valued under £5k

    GIVEN that a probate case has been submitted
    And the case will be for a "family" jurisdiction
    And the case is made through ’default’’ channel
    And the case event is ''issue'' or ''misc''
    And the case has an estate valued at £5000 and below
    When the fee for this case is being looked up through the API
    Then return a £0 fee and/or 204 message */
    @Test
    public void lookupProbateEstateOf5000AndGetNoContent() throws Exception{

        restActions
            .get("/fees-register/fees/lookup?service=probate&jurisdiction1=family&jurisdiction2=probate registry&channel=default&event=issue&applicant_type=all&amount_or_volume=10000.00")
            .andExpect(status().isOk())
            .andExpect(body().as(FeeLookupResponseDto.class, feeLookupResponseDto -> {
                assertThat(feeLookupResponseDto.getCode()).isEqualTo("FEE0219");
                assertThat(feeLookupResponseDto.getVersion()).isEqualTo(new Integer(2));
                assertThat(feeLookupResponseDto.getFeeAmount()).isEqualTo(new BigDecimal("155.00"));
            }));

    }
}
