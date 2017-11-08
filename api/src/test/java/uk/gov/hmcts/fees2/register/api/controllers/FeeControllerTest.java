package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;


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
            .post("/fees-register/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());

    }

    @Test
    public void readFeeTest() throws Exception {
        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 999, "X0002", FeeVersionStatus.approved);

        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());

        restActions
            .get("/fees-register/fees/X0002")
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, (feeDto) -> {
                assertThat(feeDto.getCode().equals("X0002"));
                assertThat(feeDto.getJurisdiction1Dto().getName().equals("civil"));
            }));
    }

    @Test
    public void approveFeeTest() throws Exception {
        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 1999, "X0003", FeeVersionStatus.draft);

        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());

        ApproveFeeDto approveFeeDto = new ApproveFeeDto();
        approveFeeDto.setFeeCode("X0003");
        approveFeeDto.setFeeVersion(1);

        restActions
            .withUser("admin")
            .patch("/fees-register/fees/approve", approveFeeDto)
            .andExpect(status().isOk());
    }

    @Test
    public void feesLookupTest() throws Exception {
        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2999, "X0004", FeeVersionStatus.approved);

        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());


        restActions
            .get("/fees-register/fees/X0004")
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, (feeDto) -> {
                assertThat(feeDto.getCode().equals("X0004"));
                assertThat(feeDto.getJurisdiction1Dto().getName().equals("civil"));
                assertThat(feeDto.getChannelTypeDto().getName().equals("online"));
            }));

//        restActions
//            .get("/fees/lookup?channel=online")
//            .andExpect(status().isOk())
//            .andExpect(body().as(FeeLookupResponseDto.class, (result) -> {
//                assertThat(result.getCode().equals("X0004"));
//            }));



    }




}
