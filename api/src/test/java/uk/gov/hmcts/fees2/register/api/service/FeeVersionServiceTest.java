package uk.gov.hmcts.fees2.register.api.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeVersionNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.model.amount.FlatAmount;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeeVersionServiceTest extends BaseIntegrationTest {

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeVersionService feeVersionService;

    @Autowired
    private FeeDtoMapper dtoMapper;

    @Autowired
    private PlatformTransactionManager transactionManager;


    @Test
    @Transactional
    public synchronized void testThatAJustCreatedAndApprovedFeeIsFoundWhenLookup() throws Exception {

        CreateRangedFeeDto dto = createDetailedFee();

        Fee fee = feeService.get(dto.getCode());

        Thread.sleep(250);

        lookupUsingUsingReferenceDataFrom(dto, new BigDecimal(5))
            .andExpect(status().isOk());

        forceDeleteFee(dto.getCode());

    }

    @Test
    @Transactional
    public synchronized void testThatSameFeeOnceExpiredFeeIsNotFoundWhenLookup() throws Exception {

        CreateRangedFeeDto dto = createDetailedFee();

        Fee fee = feeService.get(dto.getCode());

        fee.getCurrentVersion(true).setValidTo(new Date());

        Thread.sleep(250);

        lookupUsingUsingReferenceDataFrom(dto, new BigDecimal(5))
            .andExpect(status().isNotFound());

        forceDeleteFee(dto.getCode());
    }

    @Test
    @Transactional
    public synchronized void testThatWhenANewVersionIsApprovedTheCurrentOneIsMarkedForExpiration() throws Exception{

        Date date = new Date(System.currentTimeMillis() + 60000);

        CreateRangedFeeDto dto = createDetailedFee();

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setVersion(2);
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.ONE));
        versionDto.setStatus(FeeVersionStatus.pending_approval);
        versionDto.setDirection("licence");
        versionDto.setMemoLine("Hello");
        versionDto.setValidFrom(date);

        FeeVersion v = feeVersionService.save(dtoMapper.toFeeVersion(versionDto, "tarun"), dto.getCode());

        feeVersionService.changeStatus(dto.getCode(), v.getVersion(), FeeVersionStatus.approved, "tarun");

        assertEquals(date, feeService.get(dto.getCode()).getCurrentVersion(true).getValidTo());

        forceDeleteFee(dto.getCode());
    }

    @Test
    public synchronized void testThatWhenANewVersionIsApprovedAndTheCurrentOneIsMarkedForExpirationWhenItsLookedUpNewVersionIsFound() throws Exception {


        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        final CreateRangedFeeDto dto =
            transactionTemplate.execute(
                transactionStatus -> createDetailedFee()
            );

        Thread.sleep(250);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

                FeeVersionDto versionDto = new FeeVersionDto();
                versionDto.setVersion(2);
                versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.ONE));
                versionDto.setStatus(FeeVersionStatus.pending_approval);
                versionDto.setDirection("licence");
                versionDto.setMemoLine("Hello");
                versionDto.setValidFrom(new Date());

                FeeVersion v = feeVersionService.save(dtoMapper.toFeeVersion(versionDto, "tarun"), dto.getCode());

                feeVersionService.changeStatus(dto.getCode(), v.getVersion(), FeeVersionStatus.approved, "tarun");

            }
        });

        Thread.sleep(250);

        lookupUsingUsingReferenceDataFrom(dto, new BigDecimal(5))
            .andExpect(status().isOk())
            .andExpect(lookupResultMatchesExpectedFeeAmount(BigDecimal.ONE));

        forceDeleteFee(dto.getCode());
    }


    private CreateRangedFeeDto createDetailedFee() {

        CreateRangedFeeDto dto = new CreateRangedFeeDto();

        dto.setChannel("online");
        dto.setCode(String.valueOf(System.currentTimeMillis()));

        dto.setService("general");
        dto.setEvent("issue");
        dto.setJurisdiction1("civil");
        dto.setJurisdiction2("high court");
        dto.setMinRange(BigDecimal.ONE);
        dto.setMaxRange(BigDecimal.TEN);

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setVersion(1);
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setStatus(FeeVersionStatus.approved);
        versionDto.setDirection("licence");
        versionDto.setMemoLine("Hello");
        versionDto.setValidFrom(new Date());

        dto.setVersion(versionDto);

        String code = feeService.save(dtoMapper.toFee(dto, AUTHOR)).getCode();

        dto.setCode(code);

        return dto;

    }

    @Test(expected = FeeVersionNotFoundException.class)
    public void testUpdateVersion_withNonExistingFeeCode_shouldReturnException() throws Exception {
        feeVersionService.updateVersion("FEE0999", 1, null, new BigDecimal("99.01"), "cost recovery",
            "test update fee version", "memo line", "xxx", "fee order name", "si",
            "sirefid001");
    }

    @Test
    @Transactional
    public void testUpdateVersion_withExistingFeeCode() throws Exception {
        Fee fee = feeService.get("FEE0221");
        assertThat(fee.getCode()).isEqualTo("FEE0221");
        FeeVersion version = fee.getCurrentVersion(true);
        assertThat(version.getVersion()).isEqualTo(4);
        assertThat(version.getAmount()).isEqualTo(new FlatAmount(new BigDecimal("25.00")));
        assertThat(version.getStatutoryInstrument()).isEqualTo("2016 No 1191");
        assertThat(version.getSiRefId()).isEqualTo("2.1ci");
        assertThat(version.getFeeOrderName()).isEqualTo("The Civil Proceedings Fees (Amendment) Order 2016");

        feeVersionService.updateVersion("FEE0221", 5, null, new BigDecimal("99.01"), "cost recovery",
            "test update fee version", "memo line", "xxx",
            "The Civil Proceedings Fees (Amendment) Order 2017", "2017 No 1192",
            "2.1cii");
        Fee updateFee = feeService.get("FEE0221");
        FeeVersion updatedVersion = updateFee.getCurrentVersion(true);
        assertThat(updatedVersion.getVersion()).isEqualTo(5);
        assertThat(updatedVersion.getAmount()).isEqualTo(new FlatAmount(new BigDecimal("99.01")));
        assertThat(updatedVersion.getDirectionType().getName()).isEqualTo("cost recovery");
        assertThat(updatedVersion.getDescription()).isEqualTo("test update fee version");
        assertThat(updatedVersion.getFeeOrderName()).isEqualTo("The Civil Proceedings Fees (Amendment) Order 2017");
        assertThat(updatedVersion.getStatutoryInstrument()).isEqualTo("2017 No 1192");
        assertThat(updatedVersion.getSiRefId()).isEqualTo("2.1cii");




    }
}
