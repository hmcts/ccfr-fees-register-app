package uk.gov.hmcts.fees2.register.api.service;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.BaseTest;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.model.amount.FlatAmount;
import uk.gov.hmcts.fees2.register.data.repository.ChannelTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FeeServiceTest extends BaseTest{

    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeDtoMapper dtoMapper;

    @Test
    public void testBasicSearch() {

        createDefaultChannelType();
        createSimplestFee();

        List<Fee> res = feeService.search(new LookupFeeDto());

        assertTrue(res.size() > 0);

    }

    @Test
    public void testRefinedSearch() {

        createDetailedFee("divorce");

        LookupFeeDto dto = new LookupFeeDto();

        dto.setChannel("online");

        dto.setDirection("license");
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("high court");

        List<Fee> res = feeService.search(dto);

        assertTrue(res.size() == 1);

    }


    @Test
    @Transactional
    public void testRefinedLookup() {

        createDetailedFee("civil money claims");

        LookupFeeDto dto = new LookupFeeDto();

        dto.setChannel("online");

        dto.setDirection("license");
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("high court");

        Fee fee = feeService.lookup(dto);

        assertEquals( BigDecimal.TEN, fee.getCurrentVersion().calculateFee(new BigDecimal(5)));

    }


    /* --- */

    protected void createSimplestFee() {

        Fee fee = new FixedFee();

        fee.setCode(String.valueOf(System.currentTimeMillis()));

        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setVersion(1);
        feeVersion.setAmount(new FlatAmount(BigDecimal.ONE));
        fee.setFeeVersions(Arrays.asList(feeVersion));

        feeService.save(fee);

        fee = feeService.get(fee.getCode());

        assertEquals(ChannelType.DEFAULT, fee.getChannelType().getName());

    }

    private String createDetailedFee(String service) {

        RangedFeeDto dto = new RangedFeeDto();

        dto.setChannel("online");
        dto.setCode(String.valueOf(System.currentTimeMillis()));
        dto.setDirection("license");
        dto.setService(service);
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("high court");
        dto.setMemoLine("Hello");
        dto.setMinRange(BigDecimal.ONE);
        dto.setMaxRange(BigDecimal.TEN);

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setStatus(FeeVersionStatus.approved);

        dto.setVersion(versionDto);

        feeService.save(dtoMapper.toFee(dto));

        return dto.getCode();
    }

    private void createDefaultChannelType() {

        ChannelType def = channelTypeRepository.findOne(ChannelType.DEFAULT);

        if(def == null) {
            def = new ChannelType();
            def.setName(ChannelType.DEFAULT);
            channelTypeRepository.save(def);
        }

    }

}
