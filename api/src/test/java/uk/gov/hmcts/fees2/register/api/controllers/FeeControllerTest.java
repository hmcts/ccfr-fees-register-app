package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by tarun on 02/11/2017.
 */


public class FeeControllerTest extends BaseIntegrationTest {

    private String feeCode;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    /**
     * @throws Exception
     */
    @Test
    public synchronized void createFeeTest() throws Exception {
        feeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 99, feeCode, FeeVersionStatus.approved);

        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());

        deleteFee(feeCode);

    }

    @Test
    public synchronized void readFeeTest() throws Exception {
        feeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(100, 199, feeCode, FeeVersionStatus.approved);

        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());

        restActions
            .get("/fees-register/fees/" + feeCode)
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, (feeDto) -> {
                assertThat(feeDto.getCode().equals(feeCode));
                assertThat(feeDto.getJurisdiction1Dto().getName().equals("civil"));
            }));

        deleteFee(feeCode);
    }

    @Test
    public synchronized void approveFeeTest() throws Exception {
        feeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(200, 299, feeCode, FeeVersionStatus.draft);

        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto)
            .andExpect(status().is2xxSuccessful());

        ApproveFeeDto approveFeeDto = new ApproveFeeDto();
        approveFeeDto.setFeeCode(feeCode);
        approveFeeDto.setFeeVersion(1);

        restActions
            .withUser("admin")
            .patch("/fees-register/fees/approve", approveFeeDto)
            .andExpect(status().is2xxSuccessful());

        deleteFee(feeCode);
    }


    /**
     * @throws Exception
     */
    @Test
    public synchronized void feesLookupTest() throws Exception {
        feeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoForLookup(300, 399, feeCode, FeeVersionStatus.approved);


        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto)
            .andExpect(status().isCreated());

        restActions
            .get("/fees-register/fees/" + feeCode)
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, (feeDto) -> {
                assertThat(feeDto.getCode().equals(feeCode));
                assertThat(feeDto.getJurisdiction1Dto().getName().equals("civil"));
                assertThat(feeDto.getChannelTypeDto().getName().equals("online"));
            }));

        restActions
            .get("/fees-register/lookup?service=divorce&jurisdiction1=family&jurisdiction2=high court&event=copies&channel=online")
            .andExpect(status().isOk())
            .andExpect(body().as(FeeLookupResponseDto.class, (lookupDto) -> {
                assertThat(lookupDto.getCode().equals(feeCode));
                assertThat(lookupDto.getFeeAmount().equals(new BigDecimal(2500)));
            }));

        deleteFee(rangedFeeDto.getCode());

    }


    /**
     * @throws Exception
     */
    @Test
    public synchronized void feesLookupNotFoundTest() throws Exception {
        restActions
            .get("/fees-register/lookup?service=divorce&jurisdiction1=family&jurisdiction2=high court&event=copies")
            .andExpect(status().isNotFound());

    }

    /**
     * @throws Exception
     */
    @Test
    public synchronized void searchFeeTest() throws Exception {
        feeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto1 = getRangedFeeDtoWithReferenceData(500, 599, feeCode, FeeVersionStatus.approved);
        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto1)
            .andExpect(status().isCreated());

        String newFeeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto2 = getRangedFeeDtoWithReferenceData(600, 699, newFeeCode, FeeVersionStatus.draft);
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
                    assertThat(fee2Dto.getCode().equals(feeCode));
                    assertThat(fee2Dto.getFeeVersionDtos()).anySatisfy(feeVersionDto -> {
                        assertThat(feeVersionDto.getStatus().equals(FeeVersionStatus.approved));
                    });
                });
            }));

        deleteFee(feeCode);
        deleteFee(newFeeCode);

    }

    @Test
    public synchronized void createInvalidDateVersionTest() throws Exception {
        feeCode = UUID.randomUUID().toString();

        Date date = new Date();

        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 99, feeCode, FeeVersionStatus.approved);
        rangedFeeDto.getVersion().setValidTo(date);
        rangedFeeDto.getVersion().setValidFrom(date);

        restActions
            .withUser("admin")
            .post("/fees-register/rangedfees", rangedFeeDto)
            .andExpect(status().isBadRequest());

        deleteFee(feeCode);
    }

    @Test
    public synchronized void createCsvImportFixedFeesTest() throws Exception {
        restActions
            .withUser("admin")
            .post("/fees-register/bulkfixedfees", getFixedFeesDto())
            .andExpect(status().isCreated());


        restActions
            .get("/fees-register/fees/X0IMP1")
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, fee2Dto -> {
                assertThat(fee2Dto.getCode()).isEqualTo("X0IMP1");
                assertThat(fee2Dto.getChannelTypeDto().getName()).isEqualTo("default");
                assertThat(fee2Dto.getEventTypeDto().getName()).isEqualTo("issue");
                assertThat(fee2Dto.getFeeType()).isEqualTo("fixed");
                assertThat(fee2Dto.getFeeVersionDtos().get(0).getFlatAmount().getAmount()).isEqualTo(new BigDecimal("150.00"));
                assertThat(fee2Dto.getFeeVersionDtos().get(0).getStatus()).isEqualTo(FeeVersionStatus.draft);
            }));

        restActions
            .get("/fees-register/fees")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
                assertThat(fee2Dtos).anySatisfy(f -> {
                    assertThat(f.getCode()).isEqualTo("X0IMP2");
                    assertThat(f.getJurisdiction1Dto().getName()).isEqualTo("family");
                    assertThat(f.getMemoLine()).isEqualTo("Test memo line");
                    assertThat(f.getFeeVersionDtos().get(0).getFlatAmount().getAmount()).isEqualTo(new BigDecimal("300.00"));
                    assertThat(f.getFeeVersionDtos().get(0).getVersion()).isEqualTo(1);
                    assertThat(f.getFeeVersionDtos().get(0).getStatus()).isEqualTo(FeeVersionStatus.approved);
                });
            }));

        deleteFee("X0IMP1");
        deleteFee("X0IMP2");
    }

    @Test
    public synchronized void createCsvImportFixedFeesWithIncorrectDataTest() throws Exception {

        restActions
            .withUser("admin")
            .post("/fees-register/fixedfees/bulk", getIncorrectFixedFeesDto())
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void findFeeWithInvalidReferenceData() throws Exception {
        restActions
            .get("/fees-register/lookup?service=divorce&jurisdiction1=family&jurisdiction2=high court&event=copies1")
            .andExpect(status().isBadRequest());
    }

}
