package uk.gov.hmcts.fees2.register.api.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionStatusDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseTest;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.BandedFee;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.model.FixedFee;
import uk.gov.hmcts.fees2.register.data.model.RateableFee;
import uk.gov.hmcts.fees2.register.data.model.RelationalFee;
import uk.gov.hmcts.fees2.register.data.model.amount.FlatAmount;
import uk.gov.hmcts.fees2.register.data.repository.ChannelTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class FeeServiceTest extends BaseTest{

    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeDtoMapper dtoMapper;

    @Test
    @Transactional
    public void testDeletePreventsDeletingApprovedFee() {
        String code = createSimplestFee();
    }

    @Test
    @Transactional
    public void testBasicSearch() {

        createDefaultChannelType();
        String code = createSimplestFee();

        List<Fee> res = feeService.search(new LookupFeeDto());

        assertTrue(res.size() > 0);

        feeService.delete(code);
    }

    @Test
    @Transactional
    public void testRefinedSearch() {

        String code = createDetailedFee("divorce", FeeVersionStatus.approved, BigDecimal.ONE, BigDecimal.TEN);

        LookupFeeDto dto = new LookupFeeDto();

        dto.setChannel("online");

        //dto.setDirection("licence");
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("high court");

        List<Fee> res = feeService.search(dto);

        assertTrue(res.size() == 1);

        feeService.delete(code);
    }


    @Test
    @Transactional
    public void testRefinedLookup() {

        String code = createDetailedFee("civil money claims", FeeVersionStatus.approved, BigDecimal.ONE, BigDecimal.TEN);

        LookupFeeDto dto = new LookupFeeDto();

        dto.setChannel("online");

        //dto.setDirection("licence");
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("high court");
        dto.setAmountOrVolume(new BigDecimal(5));

        FeeLookupResponseDto fee = feeService.lookup(dto);

        assertEquals( BigDecimal.TEN, fee.getFeeAmount());

        feeService.delete(code);

    }

    @Test
    @Transactional
    public void testLookupSearchesOnlyApprovedFees() {
        createDetailedFee("civil money claims", FeeVersionStatus.approved, BigDecimal.ONE, BigDecimal.valueOf(5));
        createDetailedFee("civil money claims", FeeVersionStatus.draft, BigDecimal.valueOf(6), BigDecimal.TEN);

        LookupFeeDto dto = new LookupFeeDto();

        dto.setChannel("online");
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("high court");
        dto.setAmountOrVolume(new BigDecimal(5));

        //confirm properly found
        FeeLookupResponseDto fee = feeService.lookup(dto);

        assertNotEquals(null, fee);

        dto.setAmountOrVolume(new BigDecimal(6));
        try {
            feeService.lookup(dto);
        } catch (FeeNotFoundException ex) {
            assertEquals("Exception is fee not found", FeeNotFoundException.class, ex.getClass());
        }
    }

    @Test
    @Transactional
    public void testCreateFeeWithoutVersionOrStatus(){

        Fee fee = new FixedFee();

        fee.setCode(String.valueOf(System.currentTimeMillis()));

        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setAmount(new FlatAmount(BigDecimal.ONE));
        fee.setFeeVersions(Arrays.asList(feeVersion));

        feeService.save(fee);

        assertEquals(Integer.valueOf(1), fee.getFeeVersions().get(0).getVersion());
        assertEquals(FeeVersionStatus.draft, fee.getFeeVersions().get(0).getStatus());

        feeService.delete(fee.getCode());
    }

    @Test
    @Transactional
    public void testCreateBandedFeeWithoutVersionOrStatus(){

        Fee fee = new BandedFee();

        fee.setCode(String.valueOf(System.currentTimeMillis()));

        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setAmount(new FlatAmount(BigDecimal.ONE));
        fee.setFeeVersions(Arrays.asList(feeVersion));

        feeService.save(fee);

        assertEquals(Integer.valueOf(1), fee.getFeeVersions().get(0).getVersion());
        assertEquals(FeeVersionStatus.draft, fee.getFeeVersions().get(0).getStatus());

        feeService.delete(fee.getCode());
    }

    @Test
    @Transactional
    public void testCreateRelationalFeeWithoutVersionOrStatus(){

        Fee fee = new RelationalFee();

        fee.setCode(String.valueOf(System.currentTimeMillis()));

        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setAmount(new FlatAmount(BigDecimal.ONE));
        fee.setFeeVersions(Arrays.asList(feeVersion));

        feeService.save(fee);

        assertEquals(Integer.valueOf(1), fee.getFeeVersions().get(0).getVersion());
        assertEquals(FeeVersionStatus.draft, fee.getFeeVersions().get(0).getStatus());

        feeService.delete(fee.getCode());
    }

    @Test
    @Transactional
    public void testCreateRateableFeeWithoutVersionOrStatus(){

        Fee fee = new RateableFee();

        fee.setCode(String.valueOf(System.currentTimeMillis()));

        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setAmount(new FlatAmount(BigDecimal.ONE));
        fee.setFeeVersions(Arrays.asList(feeVersion));

        feeService.save(fee);

        assertEquals(Integer.valueOf(1), fee.getFeeVersions().get(0).getVersion());
        assertEquals(FeeVersionStatus.draft, fee.getFeeVersions().get(0).getStatus());

        feeService.delete(fee.getCode());
    }


    /* --- */

    protected String createSimplestFee() {

        Fee fee = new FixedFee();

        fee.setCode(String.valueOf(System.currentTimeMillis()));

        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setVersion(1);
        feeVersion.setAmount(new FlatAmount(BigDecimal.ONE));
        fee.setFeeVersions(Arrays.asList(feeVersion));

        feeService.save(fee);

        fee = feeService.getFee(fee.getCode());

        assertEquals(ChannelType.DEFAULT, fee.getChannelType().getName());

        return fee.getCode();

    }

    private String createDetailedFee(String service, FeeVersionStatus status, BigDecimal minRange, BigDecimal maxRange) {

        RangedFeeDto dto = new RangedFeeDto();

        dto.setChannel("online");
        dto.setCode(String.valueOf(System.currentTimeMillis()));

        dto.setService(service);
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("high court");
        dto.setMinRange(minRange);
        dto.setMaxRange(maxRange);

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setStatus(FeeVersionStatusDto.valueOf(status.name()));
        versionDto.setDirection("licence");
        versionDto.setMemoLine("Hello");

        dto.setVersion(versionDto);

        feeService.save(dtoMapper.toFee(dto, AUTHOR));

        return dto.getCode();
    }

    private void createDefaultChannelType() {

        ChannelType def = channelTypeRepository.getOne(ChannelType.DEFAULT);

        if(def == null) {
            def = new ChannelType();
            def.setName(ChannelType.DEFAULT);
            channelTypeRepository.save(def);
        }

    }

}
