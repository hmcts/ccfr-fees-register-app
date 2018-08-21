package uk.gov.hmcts.fees2.register.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.amount.VolumeAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.*;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by tarun on 02/11/2017.
 */


public class FeeControllerTest extends BaseIntegrationTest {

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public synchronized void readFeeTest() throws Exception {
        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(100, 199, null, FeeVersionStatus.approved);

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

        forceDeleteFee(arr[3]);
    }



    @Test
    public synchronized void createBandedFeeTest() throws Exception {
        BandedFeeDto bandedFeeDto = getBandedFeeDtoWithReferenceData(null, FeeVersionStatus.approved);

        String loc = restActions
            .withUser("admin")
            .post("/fees-register/banded-fees", bandedFeeDto)
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

        forceDeleteFee(arr[3]);
    }

    @Test
    public synchronized void createRelationalFeeTest() throws Exception {
       RelationalFeeDto relationalFeeDto = getRelationalFeeDtoWithReferenceData(null, FeeVersionStatus.approved);

        String loc = restActions
            .withUser("admin")
            .post("/fees-register/relational-fees", relationalFeeDto)
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

        forceDeleteFee(arr[3]);
    }


    @Test
    public synchronized void createRateableFeeTest() throws Exception {
        RateableFeeDto rateableFeeDto = getRateableFeeDtoWithReferenceData(null, FeeVersionStatus.approved);

        String loc = restActions
            .withUser("admin")
            .post("/fees-register/rateable-fees", rateableFeeDto)
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

        forceDeleteFee(arr[3]);
    }




    /**
     * @throws Exception
     */
    @Test
    public synchronized void feesLookupTest() throws Exception {
        RangedFeeDto rangedFeeDto = getRangedFeeDtoForLookup(300, 399, null, FeeVersionStatus.approved);
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

        forceDeleteFee(arr[3]);
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
    @Transactional
    public synchronized void searchFeeTest() throws Exception {
        loadFees();

        restActions
            .withUser("admin")
            .get("/fees-register/fees")
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

        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 99, null, FeeVersionStatus.approved);
        rangedFeeDto.getVersion().setValidTo(date);
        rangedFeeDto.getVersion().setValidFrom(date);

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isBadRequest());
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
    @Transactional
    public void findFeeWithVolume_inWholeNumber_shouldReturnValidFee() throws Exception {
        FixedFeeDto fixedFeeDto = objectMapper.readValue(getCreateProbateCopiesFeeJson().getBytes(), FixedFeeDto.class);
        String loc = restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", fixedFeeDto)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");

        MvcResult result = restActions
            .withUser("admin")
            .get("/fees-register/fees/lookup?service=probate&jurisdiction1=family&jurisdiction2=probate registry&channel=default&event=copies&applicant_type=all&amount_or_volume=3")
            .andExpect(status().isOk())
            .andReturn();

        FeeLookupResponseDto fee = objectMapper.readValue(result.getResponse().getContentAsByteArray(), FeeLookupResponseDto.class);
        assertEquals(fee.getCode(), arr[3]);
        assertEquals(fee.getDescription(), "Additional copies of the grant representation");
        assertEquals(fee.getVersion(), new Integer(1));
        assertEquals(fee.getFeeAmount(), new BigDecimal("1.50"));

        forceDeleteFee(arr[3]);
    }

    // test that delete throws 403 when trying to delete a fee with an approved version
    @Test
    public void deletingFeeWithApprovedVersionThrowsForbiddenException() throws Exception {
        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(100, 199, null, FeeVersionStatus.approved);

        String loc = restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");

        restActions
            .withUser("admin")
            .delete(URIUtils.getUrlForDeleteMethod(FeeController.class, "deleteFee"), arr[3])
            .andExpect(status().isForbidden());

        forceDeleteFee(arr[3]);
    }

    // test that delete deletes fee which has no approved versions
    @Test
    public void deletingFeeWithoutApprovedVersionReturnsNoContent() throws Exception {
        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(100, 199, null, FeeVersionStatus.draft);

        String loc = restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
        String[] arr = loc.split("/");

        restActions
            .withUser("admin")
            .delete(URIUtils.getUrlForDeleteMethod(FeeController.class, "deleteFee"), arr[3])
            .andExpect(status().isNoContent());
    }

    private void loadFees() throws Exception {
        FixedFeeDto fixedFeeDto = objectMapper.readValue(getCreateFixedFeeJson().getBytes(), FixedFeeDto.class);
        restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", fixedFeeDto)
            .andExpect(status().isCreated());

        RangedFeeDto rangedFeeDto = objectMapper.readValue(getCreateRangedFeeJson().getBytes(), RangedFeeDto.class);
        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isCreated());


    }

    private String getCreateProbateCopiesFeeJson() {
        return "{\n" +
            "      \"version\": {\n" +
            "        \"version\": 3,\n" +
            "        \"valid_from\" : \"2014-04-22T00:00:00.511Z\",\n" +
            "        \"description\": \"Additional copies of the grant representation\",\n" +
            "        \"status\": \"approved\",\n" +
            "        \"direction\": \"reduced churn\",\n" +
            "        \"statutory_instrument\": \"2014 No 876(L19)\",\n" +
            "        \"fee_order_name\": \"Non-Contentious Probate Fees\",\n" +
            "        \"si_ref_id\": \"8b\",\n" +
            "        \"memo_line\": \"Additional sealed copy of grant\",\n" +
            "        \"natural_account_code\": \"4481102171\",\n" +
            "        \"volume_amount\": {\n" +
            "          \"amount\": 0.5\n" +
            "        }\n" +
            "      },\n" +
            "      \"jurisdiction1\": \"family\",\n" +
            "      \"jurisdiction2\": \"probate registry\",\n" +
            "      \"service\": \"probate\",\n" +
            "      \"channel\": \"default\",\n" +
            "      \"event\": \"copies\",\n" +
            "      \"applicant_type\": \"all\"\n" +
            "    }";
    }

    private String getCreateRangedFeeJson() {
        return "{\n" +
            "      \"min_range\": 5000.01,\n" +
            "      \"version\": {\n" +
            "        \"version\": 1,\n" +
            "        \"valid_from\" : \"2011-04-04T00:00:00.000Z\",\n" +
            "        \"description\": \"Personal Application for grant of Probate\",\n" +
            "        \"status\": \"approved\",\n" +
            "        \"memo_line\":\"Personal Application for grant of Probate\",\n" +
            "        \"natural_account_code\":\"4481102158\",\n" +
            "        \"direction\": \"enhanced\",\n" +
            "        \"fee_order_name\": \"Non-Contentious Probate Fees\",\n" +
            "        \"statutory_instrument\":\"2011 No. 588 (L. 4)\",\n" +
            "        \"si_ref_id\": \"2\",\n" +
            "        \"flat_amount\": {\n" +
            "          \"amount\": 215.00\n" +
            "        }\n" +
            "      },\n" +
            "      \"range_unit\":\"GBP\",\n" +
            "      \"jurisdiction1\": \"family\",\n" +
            "      \"jurisdiction2\": \"probate registry\",\n" +
            "      \"service\": \"probate\",\n" +
            "      \"channel\": \"default\",\n" +
            "      \"event\": \"issue\",\n" +
            "      \"applicant_type\": \"personal\"\n" +
            "    }";
    }

    private String getCreateFixedFeeJson() {
        return "{\n" +
            "      \"version\": {\n" +
            "        \"version\": 1,\n" +
            "        \"valid_from\" : \"2014-04-22T00:00:00.000Z\",\n" +
            "        \"description\": \"Civil Court fees - Money Claims - Claim Amount - Unspecified\",\n" +
            "        \"status\": \"approved\",\n" +
            "        \"memo_line\":\"GOV - Paper fees - Money claim >£200,000\",\n" +
            "        \"natural_account_code\":\"4481102133\",\n" +
            "        \"direction\": \"enhanced\",\n" +
            "        \"flat_amount\": {\n" +
            "          \"amount\": 10000.00\n" +
            "        }\n" +
            "      },\n" +
            "      \"jurisdiction1\": \"civil\",\n" +
            "      \"jurisdiction2\": \"county court\",\n" +
            "      \"service\": \"civil money claims\",\n" +
            "      \"channel\": \"default\",\n" +
            "      \"event\": \"issue\",\n" +
            "      \"unspecified_claim_amount\": \"true\",\n" +
            "      \"applicant_type\": \"all\"\n" +
            "    }";
    }
}
