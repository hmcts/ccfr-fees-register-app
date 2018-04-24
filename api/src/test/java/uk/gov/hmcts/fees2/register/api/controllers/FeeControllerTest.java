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

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public synchronized void readFeeTest() throws Exception {
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(100, 199, null, FeeVersionStatus.approved);

        String loc = restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");

        restActions
            .get(loc)
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, (feeDto) -> {
                assertThat(feeDto.getCode().equals(arr[3]));
                assertThat(feeDto.getJurisdiction1Dto().getName().equals("civil"));
            }));

        deleteFee(arr[3]);
    }

    /**
     * @throws Exception
     */
    @Test
    public synchronized void feesLookupTest() throws Exception {
        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoForLookup(300, 399, null, FeeVersionStatus.approved);
        rangedFeeDto.getVersion().setFlatAmount(null);

        VolumeAmountDto dto = new VolumeAmountDto();
        dto.setAmount(new BigDecimal(301));

        rangedFeeDto.getVersion().setVolumeAmount(dto);

        String loc = restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");

        restActions
            .get(loc)
            .andExpect(status().isOk())
            .andExpect(body().as(Fee2Dto.class, (feeDto) -> {
                assertThat(feeDto.getCode().equals(arr[3]));
                assertThat(feeDto.getJurisdiction1Dto().getName().equals("civil"));
                assertThat(feeDto.getChannelTypeDto().getName().equals("online"));
            }));

        restActions
            .get("/fees-register/fees/lookup?service=divorce&jurisdiction1=family&jurisdiction2=high court&event=copies&channel=online&amount_or_volume=311")
            .andExpect(status().isOk())
            .andExpect(body().as(FeeLookupResponseDto.class, (lookupDto) -> {
                assertThat(lookupDto.getCode().equals(arr[3]));
                assertThat(lookupDto.getFeeAmount().equals(new BigDecimal(2500)));
            }));

        deleteFee(arr[3]);

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

        restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "search"))
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
                assertThat(fee2Dtos).anySatisfy(fee2Dto -> {
                        assertThat(fee2Dto.getFeeVersionDtos()).anySatisfy(feeVersionDto -> {
                        assertThat(feeVersionDto.getStatus().equals(FeeVersionStatus.approved));
                    });
                });
            }));

    }


    /**
     * @throws Exception
     */
    @Test
    public synchronized void searchFee_WithApprovedStatusTest() throws Exception {

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

    }

    @Test
    public synchronized void createInvalidDateVersionTest() throws Exception {
        Date date = new Date();

        CreateRangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 99, null, FeeVersionStatus.approved);
        rangedFeeDto.getVersion().setValidTo(date);
        rangedFeeDto.getVersion().setValidFrom(date);

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isBadRequest());
    }

    @Test
    public synchronized void createCsvImportFixedFeesTest() throws Exception {
        restActions
            .withUser("admin")
            .post("/fees-register/bulk-fixed-fees", getFixedFeesDto())
            .andExpect(status().isCreated());
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
        assertEquals(fee.getCode(), "FEE0003");
        assertEquals(fee.getDescription(), "Additional copies of the grant representation");
        assertEquals(fee.getVersion(), new Integer(3));
        assertEquals(fee.getFeeAmount(), new BigDecimal("1.50"));
    }

}
