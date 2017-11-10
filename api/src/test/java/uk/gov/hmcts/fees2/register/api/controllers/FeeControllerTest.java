package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;


import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by tarun on 02/11/2017.
 */


public class FeeControllerTest extends BaseIntegrationTest {

    /**
     *
     * @throws Exception
     */
    @Test
    public synchronized void createFeeTest() throws Exception{
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2000, "X0001", FeeVersionStatus.approved);

        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());

        deleteFee("X0001");

    }

    @Test
    public synchronized void readFeeTest() throws Exception {
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 999, "X0002", FeeVersionStatus.approved);

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

        deleteFee("X0002");
    }

    @Test
    public synchronized void approveFeeTest() throws Exception {
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 1999, "X0003", FeeVersionStatus.draft);

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

        deleteFee("X0003");
    }

    @Test
    public synchronized void feesLookupTest() throws Exception {
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2999, "X0004", FeeVersionStatus.approved);

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

        deleteFee("X0004");

    }

}
