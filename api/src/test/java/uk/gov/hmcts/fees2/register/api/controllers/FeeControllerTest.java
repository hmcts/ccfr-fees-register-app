package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.model.RangedFee;


import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by tarun on 02/11/2017.
 */


public class FeeControllerTest extends BaseTest {

    /**
     *
     * @throws Exception
     */
    @Test
    public void createFee() throws Exception{
        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2000, "X0026", FeeVersionStatus.approved);

        restActions
            .withUser("admin")
            .post("/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());

    }

    @Test
    @Transactional
    public void readFee() throws Exception {

        restActions
            .get("/fee/X0026")
            .andExpect(status().isOk())
            .andExpect(body().as(RangedFeeDto.class, (fee) -> {
                assertThat(fee.getCode().equals("X0026"));
                assertThat(fee.getJurisdiction1().equals("civil"));
            }));
    }
}
