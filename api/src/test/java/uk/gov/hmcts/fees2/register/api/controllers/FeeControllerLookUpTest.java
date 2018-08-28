package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.api.controllers.base.FeeDataUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class FeeControllerLookUpTest extends BaseIntegrationTest {

    @Test
    public synchronized void feesLookupNotFoundTest() throws Exception {
        restActions
            .withUser("admin")
            .get("/fees-register/fees/lookup?service=divorce&jurisdiction1=family&jurisdiction2=high court&event=copies&amount_or_volume=10&channel=default&applicant_type=all")
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void findFeeLookupWithKeyword_whenFeeHasKeyword() throws Exception {
        // given
        String feeCode = createAFeeWithKeyword("testKeyword");

        // when & then
        restActions
            .withUser("admin")
            .get("/fees-register/fees/lookup?service=probate&jurisdiction1=family&jurisdiction2=probate registry&channel=default&event=copies&applicant_type=all&amount_or_volume=3&keyword=testKeyword")
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void findFeeLookupWithWrongKeyword_whenFeeHasKeyword() throws Exception {
        // given
        String feeCode = createAFeeWithKeyword("testKeyword");

        // when & then
        restActions
            .withUser("admin")
            .get("/fees-register/fees/lookup?service=probate&jurisdiction1=family&jurisdiction2=probate registry&channel=default&event=copies&applicant_type=all&amount_or_volume=3&keyword=wrongKey")
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void findFeeLookupWithOutKeyword_whenFeeHasKeyword() throws Exception {
        // given
        String feeCode = createAFeeWithKeyword("testKeyword");

        // when & then
        restActions
            .withUser("admin")
            .get("/fees-register/fees/lookup?service=probate&jurisdiction1=family&jurisdiction2=probate registry&channel=default&event=copies&applicant_type=all&amount_or_volume=3")
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void findFeeLookupWithOutKeyword_whenOneFeeHasKeywordAndOtherNot() throws Exception {
        // given - one fee with same reference data plus keyword and another no keyword
        String feeCode = createAFeeWithKeyword("testKeyword");
        String feeCode2 = createAFeeWithKeyword(null);

        // when & then
        restActions
            .withUser("admin")
            .get("/fees-register/fees/lookup?service=probate&jurisdiction1=family&jurisdiction2=probate registry&channel=default&event=copies&applicant_type=all&amount_or_volume=3")
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void findFeeLookupWithOutKeyword_whenFeeHasNoKeyword() throws Exception {
        // given
        String feeCode = createAFeeWithKeyword(null);

        // when & then
        restActions
            .withUser("admin")
            .get("/fees-register/fees/lookup?service=probate&jurisdiction1=family&jurisdiction2=probate registry&channel=default&event=copies&applicant_type=all&amount_or_volume=3")
            .andExpect(status().isOk())
            .andReturn();
    }

    private String createAFeeWithKeyword(String keyword) throws Exception {
        FixedFeeDto fixedFeeDto = FeeDataUtils.getCreateProbateCopiesFeeRequest();
        fixedFeeDto.setKeyword(keyword);
        return createFee(fixedFeeDto);
    }

}
