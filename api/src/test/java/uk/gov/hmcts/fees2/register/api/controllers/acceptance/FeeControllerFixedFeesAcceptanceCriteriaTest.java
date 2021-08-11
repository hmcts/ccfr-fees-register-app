package uk.gov.hmcts.fees2.register.api.controllers.acceptance;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionStatusDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"idam-test"})
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

        FixedFeeDto dto = new FixedFeeDto();
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("family");
        dto.setJurisdiction2("high court");

        FeeVersionDto version = new FeeVersionDto();
        version.setDescription(version.getMemoLine());
        version.setMemoLine("description");
        version.setDirection("licence");
        version.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));

        dto.setVersion(version);

        mockIdamAPI();
        String loc = restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", dto)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
        String[] uri = loc.split("/");


        restActions
            .get("/fees-register/fees/" + uri[3])
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, feeDto -> {
                assertThat(feeDto.getCode()).isEqualTo(uri[3]);
                assertThat(feeDto.getServiceTypeDto().getName()).isEqualTo("divorce");
                assertThat(feeDto.getEventTypeDto().getName()).isEqualTo("issue");
                assertThat(feeDto.getJurisdiction1Dto().getName()).isEqualTo("family");
                assertThat(feeDto.getJurisdiction2Dto().getName()).isEqualTo("high court");
                feeDto.getFeeVersionDtos().stream().forEach(v -> {
                    assertThat(v.getFlatAmount().getAmount()).isEqualTo("10.00");
                    assertThat(v.getStatus()).isEqualTo(FeeVersionStatusDto.draft);
                });
            }));

        forceDeleteFee(uri[3]);

    }

}
