package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.util.URIUtils;


import java.math.BigDecimal;

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
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 99, "X0001", FeeVersionStatus.approved);

        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());

        deleteFee("X0001");

    }

    @Test
    public synchronized void readFeeTest() throws Exception {
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(100, 199, "X0002", FeeVersionStatus.approved);

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

        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(200, 299, "X0003", FeeVersionStatus.draft);

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


    /**
     *
     * @throws Exception
     */
    @Test
    public synchronized void feesLookupTest() throws Exception {
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoForLookup(300, 399, "X0004", FeeVersionStatus.approved);


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

        restActions
            .get("/fees-register/lookup?service=divorce&jurisdiction1=family&jurisdiction2=high court&event=copies&channel=online")
            .andExpect(status().isOk())
            .andExpect(body().as(FeeLookupResponseDto.class, (lookupDto) -> {
                assertThat(lookupDto.getCode().equals("X0004"));
                assertThat(lookupDto.getFeeAmount().equals(new BigDecimal(2500)));
            }));

    }


    /**
     *
     * @throws Exception
     */
    @Test
    public void feesLookupNotFoundTest() throws Exception {
        restActions
            .get("/fees-register/lookup?service=divorce&jurisdiction1=family&jurisdiction2=high court&event=copies")
            .andExpect(status().isNotFound());

    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void searchFeeTest() throws Exception{
        CreateRangedFeeDto rangedFeeDto1 = getRangedFeeDtoWithReferenceData(500, 599, "X0006", FeeVersionStatus.approved);
        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto1)
            .andExpect(status().isCreated());

        CreateRangedFeeDto rangedFeeDto2 = getRangedFeeDtoWithReferenceData(600, 699, "X0007", FeeVersionStatus.draft);
        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto2)
            .andExpect(status().isCreated());


        restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "search"))
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
                assertThat(fee2Dtos.size() == 2);
                assertThat(fee2Dtos).anySatisfy(fee2Dto -> {
                    assertThat(fee2Dto.getCode().equals("X0005"));
                    assertThat(fee2Dto.getMinRange().equals(new BigDecimal(1)));
                    assertThat(fee2Dto.getMaxRange().equals(new BigDecimal(999)));
                    assertThat(fee2Dto.getFeeVersionDtos()).anySatisfy(feeVersionDto -> {
                        assertThat(feeVersionDto.getStatus().equals(FeeVersionStatus.approved));
                    });
                });
            }));
    }

}
