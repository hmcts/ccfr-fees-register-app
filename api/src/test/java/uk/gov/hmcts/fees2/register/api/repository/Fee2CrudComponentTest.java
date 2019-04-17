package uk.gov.hmcts.fees2.register.api.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseTest;
import uk.gov.hmcts.fees2.register.api.controllers.base.FeeDataUtils;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.exceptions.ConflictException;
import uk.gov.hmcts.fees2.register.data.model.DirectionType;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.model.amount.FlatAmount;
import uk.gov.hmcts.fees2.register.data.service.ChannelTypeService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Created by tarun on 02/11/2017.
 */

public class Fee2CrudComponentTest extends BaseTest {

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeVersionService feeVersionService;

    @Autowired
    private FeeDtoMapper feeDtoMapper;

    private RangedFeeDto rangedFeeDto;

    @Autowired
    private ChannelTypeService channelTypeService;

    private FeeDataUtils feeDataUtils;

    /**
     *
     */
    @Test
    public void createRangedFeeTest() {
        rangedFeeDto = getRangedFeeDto(null);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto, AUTHOR));

        assertNotNull(savedFee);
        feeService.delete(savedFee.getCode());
    }


    @Test
    public void createRangedFeeWithAllReferenceDataTest() {
        rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 3000, null, FeeVersionStatus.approved);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto, AUTHOR));

        Fee2Dto feeDto = feeDtoMapper.toFeeDto(savedFee);

        FeeVersionDto feeVersionDtoResult = feeDto.getFeeVersionDtos().stream().filter(v -> v.getStatus().equals(FeeVersionStatus.approved)).findAny().orElse(null);
        assertNotNull(feeVersionDtoResult);
        assertEquals(feeVersionDtoResult.getStatus(), FeeVersionStatus.approved);
        assertEquals(feeVersionDtoResult.getDescription(), "First version description");
        assertEquals(feeVersionDtoResult.getFlatAmount().getAmount(), new BigDecimal(2500));

        feeService.delete(savedFee.getCode());
    }

    @Test
    @Transactional
    public void ReadRangedFeeWithAllReferenceDataTest() {
        // Insert a new ranged fee
        rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2000, null, FeeVersionStatus.approved);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto, AUTHOR));

        Fee fee = feeService.get(savedFee.getCode());

        Fee2Dto feeDto = feeDtoMapper.toFeeDto(fee);
        FeeVersionDto feeVersionDtoResult = feeDto.getFeeVersionDtos().stream().filter(v -> v.getStatus().equals(FeeVersionStatus.approved)).findAny().orElse(null);
        assertNotNull(feeVersionDtoResult);
        assertEquals(feeVersionDtoResult.getStatus(), FeeVersionStatus.approved);
        assertEquals(feeVersionDtoResult.getDescription(), "First version description");

        feeService.delete(savedFee.getCode());
    }

    @Test
    public void createDraftFeeAndApproveTheFeeTest() {
        // Insert a new ranged fee
        rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2999, null, FeeVersionStatus.draft);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto, AUTHOR));

        Fee fee = feeService.get(savedFee.getCode());

        boolean result = feeVersionService.approve(fee.getCode(), 1, AUTHOR);
        assertTrue(result);

        feeService.delete(savedFee.getCode());
    }

    @Test
    @Transactional
    public void createFee_withSingleFeeVersionTest() throws Exception {
        // Insert a new ranged fee
        rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2999, null, FeeVersionStatus.approved);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto, AUTHOR));

        Fee fee = feeService.get(savedFee.getCode());
        assertNotNull(fee);
        assertEquals(fee.getCode(), fee.getCode());
        FeeVersion feeVersion = fee.getCurrentVersion(true);
        assertEquals(feeVersion.getStatus(), FeeVersionStatus.approved);
        assertEquals(feeVersion.getMemoLine(), "Test memo line");
        FlatAmount flatAmount = (FlatAmount) feeVersion.getAmount();
        assertEquals(flatAmount.getAmount(), new BigDecimal(2500));

        feeService.delete(savedFee.getCode());
    }

    @Test
    @Transactional
    public void createFee_andUpdateFlatAmount() throws Exception {
        rangedFeeDto = getRangedFeeDtoWithReferenceData(499, 999, null, FeeVersionStatus.approved);
        FeeVersionDto versionDto = rangedFeeDto.getVersion();

        FlatAmountDto flatAmountDto = new FlatAmountDto();
        flatAmountDto.setAmount(new BigDecimal("99.99"));
        versionDto.setFlatAmount(flatAmountDto);
        Fee fee = feeService.save(feeDtoMapper.toFee(rangedFeeDto, AUTHOR));

        Fee savedFee = feeService.get(fee.getCode());
        assertThat(savedFee.getCode()).isEqualTo(fee.getCode());
        savedFee.getFeeVersions().stream().forEach(v -> {
            FlatAmount flatAmount = (FlatAmount) v.getAmount();
            assertThat(flatAmount.getAmount()).isEqualTo(new BigDecimal("99.99"));
        });

        FeeVersion updatedFeeVersionInfo = FeeVersion.feeVersionWith()
            .validFrom(null)
            .directionType(DirectionType.directionWith().name("cost recovery").build())
            .description("new description")
            .memoLine("new memo line")
            .naturalAccountCode("xxx")
            .feeOrderName("test fee")
            .statutoryInstrument("xxx")
            .siRefId("2.1ci")
            .build();

        feeVersionService.updateVersion(savedFee.getCode(), savedFee.getFeeVersions().get(0).getVersion(),
            savedFee.getFeeVersions().get(0).getVersion() + 1,new BigDecimal("199.99"), updatedFeeVersionInfo);
        Fee updatedFee = feeService.get(fee.getCode());
        assertThat(updatedFee.getCode()).isEqualTo(fee.getCode());
        updatedFee.getFeeVersions().stream().forEach(v -> {
            FlatAmount flatAmount = (FlatAmount) v.getAmount();
            assertThat(flatAmount.getAmount()).isEqualTo(new BigDecimal("199.99"));
        });

        feeService.delete(fee.getCode());
    }

    @Test
    @Transactional
    public void testUpdateFeeVersion() throws Exception {
        FixedFeeDto cmcUnspecifiedFee = new FeeDataUtils().getCmcUnspecifiedFee();
        Fee savedFee = feeService.save(feeDtoMapper.toFee(cmcUnspecifiedFee, AUTHOR));

        Fee fee = feeService.get(savedFee.getCode());
        FeeVersion version = fee.getCurrentVersion(true);
        assertThat(version.getDirectionType().getName()).isEqualTo("enhanced");
        assertThat(version.getDescription()).isEqualTo("Civil Court fees - Money Claims - Claim Amount - Unspecified");
        assertThat(version.getMemoLine()).isEqualTo("GOV - Paper fees - Money claim >£200,000");

        Integer newVersion = version.getVersion() + 1;
        FeeVersion updatedFeeVersionInfo = FeeVersion.feeVersionWith()
            .validFrom(version.getValidFrom())
            .directionType(DirectionType.directionWith().name("cost recovery").build())
            .description("New version description")
            .memoLine("new memo line")
            .naturalAccountCode("new nac")
            .feeOrderName("new fee order name")
            .statutoryInstrument("new si")
            .siRefId("new sirefid")
            .build();

        feeVersionService.updateVersion(fee.getCode(), version.getVersion(), newVersion, new BigDecimal("99.89"), updatedFeeVersionInfo);

        FeeVersion updateVersion = feeService.get(savedFee.getCode()).getCurrentVersion(true);
        assertThat(updateVersion.getVersion()).isEqualTo(2);
        assertThat(updateVersion.getDirectionType().getName()).isEqualTo("cost recovery");
        assertThat(updateVersion.getAmount()).isEqualTo(new FlatAmount(new BigDecimal("99.89")));
        assertThat(updateVersion.getMemoLine()).isEqualTo("new memo line");
        assertThat(updateVersion.getNaturalAccountCode()).isEqualTo("new nac");
        assertThat(updateVersion.getFeeOrderName()).isEqualTo("new fee order name");
        assertThat(updateVersion.getStatutoryInstrument()).isEqualTo("new si");
        assertThat(updateVersion.getSiRefId()).isEqualTo("new sirefid");

        feeService.delete(savedFee.getCode());
    }

    @Test(expected = ConflictException.class)
    @Transactional
    public void createDuplicateCmcUnspecifiedFeeFailureTest() throws Exception {
        FixedFeeDto cmcUnspecifiedFee = new FeeDataUtils().getCmcUnspecifiedFee();
        Fee savedFee = feeService.save(feeDtoMapper.toFee(cmcUnspecifiedFee, AUTHOR));

        assertNotNull(savedFee);

        cmcUnspecifiedFee.getVersion().setFlatAmount(new FlatAmountDto(new BigDecimal("40000.00")));
        feeService.save(feeDtoMapper.toFee(cmcUnspecifiedFee, AUTHOR));
    }

    @Test
    @Transactional
    public void createCmcUnspecifiedFeeWithSameReferenceDataAndKeywordSuccessTest() throws Exception {
        FixedFeeDto cmcUnspecifiedFee = new FeeDataUtils().getCmcUnspecifiedFee();
        Fee savedFee1 = feeService.save(feeDtoMapper.toFee(cmcUnspecifiedFee, AUTHOR));
        assertNotNull(savedFee1);

        cmcUnspecifiedFee.setKeyword("KY-1");
        cmcUnspecifiedFee.getVersion().setFlatAmount(new FlatAmountDto(new BigDecimal("40000.00")));
        Fee savedFee2 = feeService.save(feeDtoMapper.toFee(cmcUnspecifiedFee, AUTHOR));
        assertNotNull(savedFee2);
    }

}
