package uk.gov.hmcts.fees2.register.api.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseTest;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class FeeMapperIntegrationTest extends BaseTest {

    @Autowired
    private FeeDtoMapper dtoMapper;

    @Test
    public void testThatAllFieldsAreCoveredInModelDtoLifecycle() {

        CreateRangedFeeDto rangedFeeDto =
            new CreateRangedFeeDto()
                .setMinRange(BigDecimal.ZERO)
                .setMaxRange(BigDecimal.TEN)
                .setRangeUnit("GBP")
                .setCode("XXXYYY")
                .setVersion(getFeeVersionDto(FeeVersionStatus.draft, "Test memo line", "CMC online fee order name",
                    "Natural code 001", "SI", "siRefId", directionTypeService.findByNameOrThrow("enhanced")))
                .setJurisdiction1(jurisdiction1Service.findByNameOrThrow("civil").getName())
                .setJurisdiction2(jurisdiction2Service.findByNameOrThrow("county court").getName())
                .setEvent(eventTypeService.findByNameOrThrow("issue").getName())
                .setService(serviceTypeService.findByNameOrThrow("civil money claims").getName())
                .setChannel(channelTypeService.findByNameOrThrow("online").getName());

        Fee fee = dtoMapper.toFee(rangedFeeDto, AUTHOR);

        Fee2Dto fee2Dto = dtoMapper.toFeeDto(fee);

        assertEquals(fee2Dto.getCode(), rangedFeeDto.getCode());
        assertEquals(fee2Dto.getMinRange(), rangedFeeDto.getMinRange());
        assertEquals(fee2Dto.getMaxRange(), rangedFeeDto.getMaxRange());
        assertEquals(fee2Dto.getRangeUnit(), rangedFeeDto.getRangeUnit());
        assertEquals(fee2Dto.getJurisdiction1Dto().getName(), rangedFeeDto.getJurisdiction1());
        assertEquals(fee2Dto.getJurisdiction2Dto().getName(), rangedFeeDto.getJurisdiction2());
        assertEquals(fee2Dto.getServiceTypeDto().getName(), rangedFeeDto.getService());
        assertEquals(fee2Dto.getChannelTypeDto().getName(), rangedFeeDto.getChannel());
        assertEquals(fee2Dto.getEventTypeDto().getName(), rangedFeeDto.getEvent());

    }

}
