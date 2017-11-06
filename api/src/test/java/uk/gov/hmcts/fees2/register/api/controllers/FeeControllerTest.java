package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
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
    public void createFeeTest() throws Exception{
        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2000, "X0001", FeeVersionStatus.approved);

        restActions
            .withUser("admin")
            .post("/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());

    }

    @Test
    public void readFeeTest() throws Exception {
        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 1999, "X0002", FeeVersionStatus.approved);

        restActions
            .withUser("admin")
            .post("/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());

        restActions
            .get("/fee/X0002")
            .andExpect(status().isOk())
            .andExpect(body().as(RangedFeeDto.class, (fee) -> {
                assertThat(fee.getCode().equals("X0002"));
                assertThat(fee.getJurisdiction1().equals("civil"));
            }));
    }

//    @Test
//    public void approveFeeTest() throws Exception {
//        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 1999, "X0003", FeeVersionStatus.draft);
//
//        restActions
//            .withUser("admin")
//            .post("/rangedfees", rangedFeeDto)
//            .andExpect(status().isCreated());
//
//        ApproveFeeDto approveFeeDto = new ApproveFeeDto();
//        approveFeeDto.setFeeCode("X0003");
//        approveFeeDto.setFeeVersion(1);
//
//        restActions
//            .withUser("admin")
//            .patch("/fee/approve", approveFeeDto)
//            .andExpect(status().isOk())
//            .andExpect(body().as(boolean.class, (result) -> {
//                assertThat(result.booleanValue());
//            }));
//    }
}
