package uk.gov.hmcts.fees2.register.api.controllers;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.VolumeAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.*;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.api.controllers.base.FeeDataUtils;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by tarun on 02/11/2017.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles({"idam-test"})
@SpringBootTest(webEnvironment = MOCK)
@Transactional
public class FeeControllerTest extends BaseIntegrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public synchronized void readFeeTest() throws Exception {
        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(100, 199, null, FeeVersionStatus.approved);

        String loc = saveFeeAndCheckStatusIsCreated(rangedFeeDto);
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

        String loc = saveFeeAndCheckStatusIsCreated(bandedFeeDto);
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

        String loc = saveFeeAndCheckStatusIsCreated(relationalFeeDto);
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

        String loc = saveFeeAndCheckStatusIsCreated(rateableFeeDto);
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

        String loc = saveFeeAndCheckStatusIsCreated(rangedFeeDto);
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
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        RangedFeeDto rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 99, null, FeeVersionStatus.approved);
        rangedFeeDto.getVersion().setValidTo(calendar.getTime());
        rangedFeeDto.getVersion().setValidFrom(date);

        mockIdamAPI();

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isBadRequest());
    }


    @Test
    public synchronized void createCsvImportFixedFeesWithIncorrectDataTest() throws Exception {

        restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees/bulk", FeeDataUtils.getIncorrectFixedFeesDto())
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
        FixedFeeDto fixedFeeDto = FeeDataUtils.getCreateProbateCopiesFeeRequest();
        String loc = saveFeeAndCheckStatusIsCreated(fixedFeeDto);
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

        String loc = saveFeeAndCheckStatusIsCreated(rangedFeeDto);
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

        String loc = saveFeeAndCheckStatusIsCreated(rangedFeeDto);
        String[] arr = loc.split("/");

        restActions
            .withUser("admin")
            .delete(URIUtils.getUrlForDeleteMethod(FeeController.class, "deleteFee"), arr[3])
            .andExpect(status().isNoContent());
    }

    private void loadFees() throws Exception {
        FixedFeeDto fixedFeeDto = FeeDataUtils.getCreateFixedFeeRequest();
        saveFeeAndCheckStatusIsCreated(fixedFeeDto);

        RangedFeeDto rangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();
        saveFeeAndCheckStatusIsCreated(rangedFeeDto);
    }


    @Test
    @Transactional
    public void createNewProbateCopiesFeeWithExistingReferenceDataFailureTest() throws Exception {
        FixedFeeDto fixedFeeDto = FeeDataUtils.getCreateFixedFeeRequest();

        String locHeader = saveFeeAndCheckStatusIsCreated(fixedFeeDto);
        String[] arr = locHeader.split("/");

        assertNotNull(arr);

        restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", fixedFeeDto)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause", is("Fee with the given reference data/overlapping range already exists")));

        forceDeleteFee(arr[3]);
    }


    @Test
    @Transactional
    public void createNewProbateCopiesFeeWithExistingReferenceDataAndNewKeywordSuccessTest() throws Exception {
        FixedFeeDto fixedFeeDto = FeeDataUtils.getCreateProbateCopiesFeeRequest();

        String locHeader = saveFeeAndCheckStatusIsCreated(fixedFeeDto);
        String[] fee1 = locHeader.split("/");

        assertNotNull(fee1);

        fixedFeeDto.setKeyword("KY-1");
        String newLocHeader = saveFeeAndCheckStatusIsCreated(fixedFeeDto);
        String[] fee2 = newLocHeader.split("/");

        assertNotNull(fee1);

        forceDeleteFee(fee1[3]);
        forceDeleteFee(fee2[3]);
    }


    @Test
    @Transactional
    public void createNewProbateCopiesFeeWithExistingReferenceDataAndKeywordFailureTest() throws Exception {
        FixedFeeDto fixedFeeDto = FeeDataUtils.getCreateProbateCopiesFeeRequest();
        fixedFeeDto.setKeyword("KY-1");

        String newLocHeader = saveFeeAndCheckStatusIsCreated(fixedFeeDto);
        String[] fee1 = newLocHeader.split("/");

        assertNotNull(fee1);

        fixedFeeDto.getVersion().setFlatAmount(new FlatAmountDto(new BigDecimal("123.98")));
        restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", fixedFeeDto)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause", is("Fee with the given reference data/overlapping range already exists")));
    }

    @Test
    @Transactional
    public void createNewFixedFeeWithKeywordTest() throws Exception {
        List<FixedFeeDto> fixedFeeDtos = FeeDataUtils.getCreateFixedFeesWithKeywordRequest();

        List<String> feeCodes = new ArrayList<>();
        fixedFeeDtos.stream()
            .forEach(f -> {
                try {
                    String header = saveFeeAndCheckStatusIsCreated(f);

                    String[] arr = header.split("/");
                    feeCodes.add(arr[3]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        assertNotNull(feeCodes);
        assertEquals(feeCodes.size(),2);

        feeCodes.stream().forEach(f -> {
            forceDeleteFee(f);
        });
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

    // min null
    // min null
    @Test
    @Transactional
    public void rejectAddingDuplicateRangedFeeMinSameForBothMaxInfinityForBothTest() throws Exception {
        RangedFeeDto firstRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();
        RangedFeeDto secondRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();

        firstRangedFeeDto.setMinRange(new BigDecimal("0"));
        firstRangedFeeDto.setMaxRange(null);
        secondRangedFeeDto.setMinRange(new BigDecimal("0"));
        secondRangedFeeDto.setMaxRange(null);

        mockIdamAPI();

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", firstRangedFeeDto)
            .andExpect(status().isCreated());

        assertNotNull(restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", secondRangedFeeDto)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause", is("Fee with the given reference data/overlapping range already exists")))
            .andReturn());
    }

    // min1 null
    // min2 null
    @Test
    @Transactional
    public void rejectAddingDuplicateRangedFeeMinDifferentForBothMaxInfinityForBothTest() throws Exception {
        RangedFeeDto firstRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();
        RangedFeeDto secondRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();

        firstRangedFeeDto.setMinRange(new BigDecimal("10"));
        firstRangedFeeDto.setMaxRange(null);
        secondRangedFeeDto.setMinRange(new BigDecimal("0"));
        secondRangedFeeDto.setMaxRange(null);

        mockIdamAPI();

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", firstRangedFeeDto)
            .andExpect(status().isCreated());

        assertNotNull(restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", secondRangedFeeDto)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause", is("Fee with the given reference data/overlapping range already exists")))
            .andReturn());
    }

    // min max
    // minBetween null
    @Test
    @Transactional
    public void rejectAddingDuplicateRangedFeeMinMaxSetForFirstMinBetweenRangeToInfinityForSecondTest() throws Exception {
        RangedFeeDto firstRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();
        RangedFeeDto secondRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();

        firstRangedFeeDto.setMinRange(new BigDecimal("0"));
        firstRangedFeeDto.setMaxRange(new BigDecimal("100"));
        secondRangedFeeDto.setMinRange(new BigDecimal("50"));
        secondRangedFeeDto.setMaxRange(null);

        mockIdamAPI();

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", firstRangedFeeDto)
            .andExpect(status().isCreated());

        assertNotNull(restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", secondRangedFeeDto)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause", is("Fee with the given reference data/overlapping range already exists")))
            .andReturn());
    }

    // min max
    // min max
    @Test
    @Transactional
    public void rejectAddingDuplicateRangedFeeMinMaxSetSameForBothTest() throws Exception {
        RangedFeeDto firstRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();
        RangedFeeDto secondRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();

        firstRangedFeeDto.setMinRange(new BigDecimal("0"));
        firstRangedFeeDto.setMaxRange(new BigDecimal("100"));
        secondRangedFeeDto.setMinRange(new BigDecimal("0"));
        secondRangedFeeDto.setMaxRange(new BigDecimal("100"));

        mockIdamAPI();

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", firstRangedFeeDto)
            .andExpect(status().isCreated());

        assertNotNull(restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", secondRangedFeeDto)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause", is("Fee with the given reference data/overlapping range already exists")))
            .andReturn());
    }

    // min max
    // minBetween maxAbove
    @Test
    @Transactional
    public void rejectAddingDuplicateRangedFeeMinMaxForFirstMinBetweenAndMaxAboveForSecondTest() throws Exception {
        RangedFeeDto firstRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();
        RangedFeeDto secondRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();

        firstRangedFeeDto.setMinRange(new BigDecimal("0"));
        firstRangedFeeDto.setMaxRange(new BigDecimal("100"));
        secondRangedFeeDto.setMinRange(new BigDecimal("50"));
        secondRangedFeeDto.setMaxRange(new BigDecimal("150"));

        mockIdamAPI();

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", firstRangedFeeDto)
            .andExpect(status().isCreated());

        assertNotNull(restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", secondRangedFeeDto)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause", is("Fee with the given reference data/overlapping range already exists")))
            .andReturn());
    }

    // min max
    // minBelow maxBetween
    @Test
    @Transactional
    public void rejectAddingDuplicateRangedFeeFirstMinMaxSecondMinBelowMaxBetweenTest() throws Exception {
        RangedFeeDto firstRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();
        RangedFeeDto secondRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();

        firstRangedFeeDto.setMinRange(new BigDecimal("25"));
        firstRangedFeeDto.setMaxRange(new BigDecimal("100"));
        secondRangedFeeDto.setMinRange(new BigDecimal("0"));
        secondRangedFeeDto.setMaxRange(new BigDecimal("50"));

        mockIdamAPI();

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", firstRangedFeeDto)
            .andExpect(status().isCreated());

        assertNotNull(restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", secondRangedFeeDto)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause", is("Fee with the given reference data/overlapping range already exists")))
            .andReturn());
    }

    // min max1
    // max1 max2
    @Test
    @Transactional
    public void rejectAddingDuplicateRangedFeeFirstMinMax1SecondMax1Max2Test() throws Exception {
        RangedFeeDto firstRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();
        RangedFeeDto secondRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();

        firstRangedFeeDto.setMinRange(new BigDecimal("0"));
        firstRangedFeeDto.setMaxRange(new BigDecimal("50"));
        secondRangedFeeDto.setMinRange(new BigDecimal("50"));
        secondRangedFeeDto.setMaxRange(new BigDecimal("100"));

        mockIdamAPI();

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", firstRangedFeeDto)
            .andExpect(status().isCreated());

        assertNotNull(restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", secondRangedFeeDto)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause", is("Fee with the given reference data/overlapping range already exists")))
            .andReturn());
    }



    private String createAFeeWithKeyword(String keyword) throws Exception {
        FixedFeeDto fixedFeeDto = FeeDataUtils.getCreateProbateCopiesFeeRequest();
        fixedFeeDto.setKeyword(keyword);
        return createFee(fixedFeeDto);
    }


    @Test
    @Transactional
    public void rejectAddingDuplicateFeeMetadataOnDifferentTypes() throws Exception {

        FixedFeeDto fixedFeeDto = FeeDataUtils.getCreateProbateCopiesFeeRequest();
        fixedFeeDto.setKeyword("xxx");

        RangedFeeDto rangedFeeDto = FeeDataUtils.getRangedCreateProbateCopiesFeeRequest();
        rangedFeeDto.setKeyword("xxx");

        mockIdamAPI();

        restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", fixedFeeDto)
            .andExpect(status().isCreated());

        assertNotNull(restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", rangedFeeDto)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause", is("Fee with the given reference data/overlapping range already exists")))
            .andReturn());

    }


    // min max
    // minBelow maxAbove
    @Test
    @Transactional
    public void rejectAddingDuplicateRangedFeeFirstMinMaxSecondMinBelowMaxAboveTest() throws Exception {
        RangedFeeDto firstRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();
        RangedFeeDto secondRangedFeeDto = FeeDataUtils.getCreateRangedFeeRequest();

        firstRangedFeeDto.setMinRange(new BigDecimal("25"));
        firstRangedFeeDto.setMaxRange(new BigDecimal("75"));
        secondRangedFeeDto.setMinRange(new BigDecimal("0"));
        secondRangedFeeDto.setMaxRange(new BigDecimal("100"));

        mockIdamAPI();

        restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", firstRangedFeeDto)
            .andExpect(status().isCreated());

        assertNotNull(restActions
            .withUser("admin")
            .post("/fees-register/ranged-fees", secondRangedFeeDto)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause", is("Fee with the given reference data/overlapping range already exists")))
            .andReturn());
    }

    @Test
    @Transactional
    public void testValidatingDuplicateFeeMetadataOnDifferentTypes() throws Exception {

        FixedFeeDto dto = FeeDataUtils.getCreateProbateCopiesFeeRequest();
        dto.setKeyword("xxx");

        mockIdamAPI();

        restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", dto)
            .andExpect(status().isCreated());

        assertNotNull(
        restActions
            .withUser("admin")
            .get("/fees-register/fees/prevalidate"
                + "?service=" + dto.getService()
                + "&event=" + dto.getEvent()
                + "&channel=" + dto.getChannel()
                + "&keyword=" + dto.getKeyword()
                + "&jurisdiction1=" + dto.getJurisdiction1()
                + "&jurisdiction2=" + dto.getJurisdiction2()
                + "&fromRange=1&toRange=1000")
            .andExpect(status().isConflict())
        );

    }

    @Test
    @Transactional
    public void testValidatingANewFeeBeforeInsertingItWorks() throws Exception {

        FixedFeeDto dto = FeeDataUtils.getCreateProbateCopiesFeeRequest();
        dto.setKeyword("xxx");

        mockIdamAPI();

        assertNotNull(
            restActions
                .withUser("admin")
                .get("/fees-register/fees/prevalidate"
                    + "?service=" + dto.getService()
                    + "&event=" + dto.getEvent()
                    + "&channel=" + dto.getChannel()
                    + "&keyword=" + dto.getKeyword()
                    + "&jurisdiction1=" + dto.getJurisdiction1()
                    + "&jurisdiction2=" + dto.getJurisdiction2()
                )
                .andExpect(status().isOk())
        );

        assertNotNull(restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", dto)
            .andExpect(status().isCreated())
            .andReturn());
    }

    @Test
    @Transactional
    public void searchFee_WithDiscontinuedFlagTrue() throws Exception {
        FixedFeeDto fixedFeeDto1 = FeeDataUtils.getCreateFixedFeeRequest();
        saveFeeAndCheckStatusIsCreated(fixedFeeDto1);

        FixedFeeDto fixedFeeDto2 = FeeDataUtils.getCreateFixedFeeRequest();
        fixedFeeDto2.setKeyword("testFixedDtoFee");
        // discontinued fee
        fixedFeeDto2.getVersion().setValidTo(DateUtils.addDays(new Date(), -1));
        saveFeeAndCheckStatusIsCreated(fixedFeeDto2);

        restActions
            .withUser("admin")
            .get("/fees-register/fees?discontinued=true")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(1)))
            .andReturn();
    }

    @Test
    @Transactional
    public void searchFee_WithDiscontinuedFlagFlag() throws Exception {
        FixedFeeDto fixedFeeDto1 = FeeDataUtils.getCreateFixedFeeRequest();
        saveFeeAndCheckStatusIsCreated(fixedFeeDto1);

        FixedFeeDto fixedFeeDto2 = FeeDataUtils.getCreateFixedFeeRequest();
        fixedFeeDto2.setKeyword("testFixedDtoFee");
        // discontinued fee
        fixedFeeDto2.getVersion().setValidFrom(DateUtils.addDays(new Date(), -100));
        fixedFeeDto2.getVersion().setValidTo(DateUtils.addDays(new Date(), -10));
        saveFeeAndCheckStatusIsCreated(fixedFeeDto2);

        restActions
            .withUser("admin")
            .get("/fees-register/fees?discontinued=false")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(1)))
            .andReturn();

    }

}
