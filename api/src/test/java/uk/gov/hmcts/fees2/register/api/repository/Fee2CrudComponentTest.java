package uk.gov.hmcts.fees2.register.api.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.BaseTest;
import uk.gov.hmcts.fees2.register.api.controllers.advice.exception.BadRequestException;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.ChannelTypeService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.Assert.*;


/**
 * Created by tarun on 02/11/2017.
 */

public class Fee2CrudComponentTest extends BaseTest {

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeDtoMapper feeDtoMapper;

    private RangedFeeDto rangedFeeDto;

    @Autowired
    private ChannelTypeService channelTypeService;


    /**
     *
     */
    @Test
    public void createRangedFeeTest() {
        rangedFeeDto = getRangedFeeDto();
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto));

        assertNotNull(savedFee);
    }

    @Test(expected = BadRequestException.class)
    public void duplicateFeeCreationTest() {
        rangedFeeDto = getRangedFeeDto();
        feeService.save(feeDtoMapper.toFee(rangedFeeDto));
    }

    @Test
    public void createRangedFeeWithAllReferenceData() {
        rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 3000, "X0025", FeeVersionStatus.draft);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto));

        RangedFeeDto rangedFeeDto = feeDtoMapper.toFeeDto(savedFee);

        assertEquals(rangedFeeDto.getCode(), "X0025");
        FeeVersionDto feeVersionDtoResult = rangedFeeDto.getFeeVersionDtos().stream().filter(v -> v.getStatus().equals(FeeVersionStatus.approved)).findAny().orElse(null);
        assertNotNull(feeVersionDtoResult);
        assertEquals(feeVersionDtoResult.getStatus(), FeeVersionStatus.approved);
        assertEquals(feeVersionDtoResult.getDescription(), "First version description");
    }

    @Test
    public void lookUpTheCreatedRangedFeeWithAllReferenceData() {
        Fee lookUpFee = feeService.get("X0025");

        RangedFeeDto rangedFeeDto = feeDtoMapper.toFeeDto(lookUpFee);
        assertEquals(rangedFeeDto.getCode(), "X0025");
        FeeVersionDto feeVersionDtoResult = rangedFeeDto.getFeeVersionDtos().stream().filter(v -> v.getStatus().equals(FeeVersionStatus.approved)).findAny().orElse(null);
        assertNotNull(feeVersionDtoResult);
        assertEquals(feeVersionDtoResult.getStatus(), FeeVersionStatus.approved);
        assertEquals(feeVersionDtoResult.getDescription(), "First version description");
        assertEquals(feeVersionDtoResult.getFlatAmount(), new BigDecimal(2500));


    }
}
