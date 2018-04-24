package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;

import uk.gov.hmcts.fees2.register.api.contract.amount.VolumeAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
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
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isCreated());

        forceDeleteFee(feeCode);

    }

    @Test
    public synchronized void readFeeTest() throws Exception {
        feeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(100, 199, feeCode, FeeVersionStatus.approved);

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isCreated());

        restActions
            .get("/fees-register/fees/" + feeCode)
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, (feeDto) -> {
                assertThat(feeDto.getCode().equals(feeCode));
                assertThat(feeDto.getJurisdiction1Dto().getName().equals("civil"));
            }));

        forceDeleteFee(feeCode);
    }

    @Test
    public synchronized void approveFeeTest() throws Exception {
        feeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(200, 299, feeCode, FeeVersionStatus.pending_approval);

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().is2xxSuccessful());

        restActions
            .withUser("admin")
            .patch("/fees/" + feeCode + "/versions/1/status/approved", "")
            .andExpect(status().is2xxSuccessful());

        forceDeleteFee(feeCode);
    }


    /**
     * @throws Exception
     */
    @Test
    public synchronized void feesLookupTest() throws Exception {
        feeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoForLookup(300, 399, feeCode, FeeVersionStatus.approved);
        rangedFeeDto.getVersion().setFlatAmount(null);

        VolumeAmountDto dto = new VolumeAmountDto();
        dto.setAmount(new BigDecimal(301));

        rangedFeeDto.getVersion().setVolumeAmount(dto);

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
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
            .get("/fees-register/fees/lookup?service=divorce&jurisdiction1=family&jurisdiction2=high court&event=copies&channel=online&amount_or_volume=311")
            .andExpect(status().isOk())
            .andExpect(body().as(FeeLookupResponseDto.class, (lookupDto) -> {
                assertThat(lookupDto.getCode().equals(feeCode));
                assertThat(lookupDto.getFeeAmount().equals(new BigDecimal(2500)));
            }));

        forceDeleteFee(rangedFeeDto.getCode());

    }


    /**
     * @throws Exception
     */
    @Test
    public synchronized void feesLookupNotFoundTest() throws Exception {
        restActions
            .withUser("admin")
            .get("/fees-register/fees/lookup?service=divorce&jurisdiction1=family&jurisdiction2=high court&event=copies&amount_or_volume=10&channel=default&applicant_type=all")
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
            .post("/fees-register/ranged-fees", rangedFeeDto1)
            .andExpect(status().isCreated());

        String newFeeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto2 = getRangedFeeDtoWithReferenceData(600, 699, newFeeCode, FeeVersionStatus.draft);
        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto2)
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

        forceDeleteFee(feeCode);
        forceDeleteFee(newFeeCode);

    }


    /**
     * @throws Exception
     */
    @Test
    public synchronized void searchFee_WithApprovedStatusTest() throws Exception {
        feeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto1 = getRangeFeeDtoForSearch(0, 100, feeCode, FeeVersionStatus.approved, "civil money claims");
        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto1)
            .andExpect(status().isCreated());

        String feeCode2 = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto2 = getRangeFeeDtoForSearch(101, 599, feeCode2, FeeVersionStatus.approved, "civil money claims");
        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto2)
            .andExpect(status().isCreated());


        String feeCode3 = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto3 = getRangeFeeDtoForSearch(101, 699, feeCode3, FeeVersionStatus.draft, "probate");
        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto3)
            .andExpect(status().isCreated());

        String feeCode4 = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto4 = getRangeFeeDtoForSearch(700, 999, feeCode4, FeeVersionStatus.pending_approval, "divorce");
        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto4)
            .andExpect(status().isCreated());

        restActions
            .withUser("admin")
            .get("/fees-register/fees?service=civil money claims&jurisdiction1=civil&jurisdiction2=county court&channel=default&event=issue&unspecifiedClaimAmounts=false&feeVersionStatus=approved")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
                fee2Dtos.stream().forEach(f -> {
                    assertEquals(f.getServiceTypeDto().getName(), "civil money claims");
                    f.getFeeVersionDtos().stream().forEach(v -> {
                        assertEquals(v.getStatus(), FeeVersionStatus.approved);
                    });
                });
            }));

        forceDeleteFee(feeCode);
        forceDeleteFee(feeCode2);
        forceDeleteFee(feeCode3);
        forceDeleteFee(feeCode4);

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
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isBadRequest());

        forceDeleteFee(feeCode);
    }

    @Test
    public synchronized void createCsvImportFixedFeesTest() throws Exception {
        restActions
            .withUser("admin")
            .post("/fees-register/bulk-fixed-fees", getFixedFeesDto())
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
                    //assertThat(f.getMemoLine()).isEqualTo("Test memo line");
                    assertThat(f.getFeeVersionDtos().get(0).getFlatAmount().getAmount()).isEqualTo(new BigDecimal("300.00"));
                    assertThat(f.getFeeVersionDtos().get(0).getVersion()).isEqualTo(1);
                    assertThat(f.getFeeVersionDtos().get(0).getStatus()).isEqualTo(FeeVersionStatus.approved);
                });
            }));

        forceDeleteFee("X0IMP1");
        forceDeleteFee("X0IMP2");
    }

    @Test
    public synchronized void createCsvImportFixedFeesWithIncorrectDataTest() throws Exception {

        restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees/bulk", getIncorrectFixedFeesDto())
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void findFeeWithInvalidReferenceData() throws Exception {
        restActions
            .withUser("admin")
            .get("/fees-register/fees/lookup?service=divorce&jurisdiction1=family&jurisdiction2=high court&event=copies1")
            .andExpect(status().isBadRequest());
    }

    @Test
    public void findFeeWithVolume_inFractions_shouldThrowBadRequestException() throws Exception {
        MvcResult result = restActions
            .withUser("admin")
            .get("/fees-register/fees/lookup?service=probate&jurisdiction1=family&jurisdiction2=probate registry&channel=default&event=copies&applicant_type=all&amount_or_volume=1.5")
            .andExpect(status().isBadRequest())
            .andReturn();

        assertEquals(result.getResponse().getContentAsString(), "{\"cause\":\"Volume cannot be in fractions.\"}");
    }

    @Test
    public void findFeeWithVolume_inWholeNumber_shouldReturnValidFee() throws Exception {
        MvcResult result = restActions
            .withUser("admin")
            .get("/fees-register/fees/lookup?service=probate&jurisdiction1=family&jurisdiction2=probate registry&channel=default&event=copies&applicant_type=all&amount_or_volume=3")
            .andExpect(status().isOk())
            .andReturn();

        FeeLookupResponseDto fee = objectMapper.readValue(result.getResponse().getContentAsByteArray(), FeeLookupResponseDto.class);
        assertEquals(fee.getCode(), "X0258");
        assertEquals(fee.getDescription(), "Additional copies of the grant representation");
        assertEquals(fee.getVersion(), new Integer(3));
        assertEquals(fee.getFeeAmount(), new BigDecimal("1.50"));
    }

    // test that delete throws 403 when trying to delete a fee with an approved version
    @Test
    public void deletingFeeWithApprovedVersionThrowsForbiddenException() throws Exception {
        feeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(100, 199, feeCode, FeeVersionStatus.approved);

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isCreated());

        restActions
            .withUser("admin")
            .delete(URIUtils.getUrlForDeleteMethod(FeeController.class, "deleteFee"), feeCode)
            .andExpect(status().isForbidden());

        forceDeleteFee(feeCode);
    }

    // test that delete deletes fee which has no approved versions
    @Test
    public void deletingFeeWithoutApprovedVersionReturnsNoContent() throws Exception {
        feeCode = UUID.randomUUID().toString();
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(100, 199, feeCode, FeeVersionStatus.draft);

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isCreated());

        restActions
            .withUser("admin")
            .delete(URIUtils.getUrlForDeleteMethod(FeeController.class, "deleteFee"), feeCode)
            .andExpect(status().isNoContent());
    }

}
