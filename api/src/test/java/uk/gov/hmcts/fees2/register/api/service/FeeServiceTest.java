package uk.gov.hmcts.fees2.register.api.service;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.BaseTest;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
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
        String code = createSimplestFee();

        List<Fee> res = feeService.search(new LookupFeeDto());

        assertTrue(res.size() > 0);

        feeService.delete(code);
    }

    @Test
    public void testRefinedSearch() {

        String code = createDetailedFee("divorce");

        LookupFeeDto dto = new LookupFeeDto();

        dto.setChannel("online");

        dto.setDirection("license");
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

        String code = createDetailedFee("civil money claims");

        LookupFeeDto dto = new LookupFeeDto();

        dto.setChannel("online");

        dto.setDirection("license");
        dto.setService("civil money claims");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("high court");
        dto.setAmount(new BigDecimal(5));

        FeeLookupResponseDto fee = feeService.lookup(dto);

        assertEquals( BigDecimal.TEN, fee.getFeeAmount());

        feeService.delete(code);

    }

    @Test
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


    /* --- */

    protected String createSimplestFee() {

        Fee fee = new FixedFee();

        fee.setCode(String.valueOf(System.currentTimeMillis()));

        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setVersion(1);
        feeVersion.setAmount(new FlatAmount(BigDecimal.ONE));
        fee.setFeeVersions(Arrays.asList(feeVersion));

        feeService.save(fee);

        fee = feeService.get(fee.getCode());

        assertEquals(ChannelType.DEFAULT, fee.getChannelType().getName());

        feeService.delete(fee.getCode());

        return fee.getCode();

    }

    private String createDetailedFee(String service) {

        CreateRangedFeeDto dto = new CreateRangedFeeDto();

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
