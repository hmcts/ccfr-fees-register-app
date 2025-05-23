package uk.gov.hmcts.fees2.register.api.service;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionStatusDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.model.FixedFee;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

        RangedFeeDto dto = createDetailedFee();

        Fee fee = feeService.getFee(dto.getCode());

        Thread.sleep(250);

        lookupUsingUsingReferenceDataFrom(dto, new BigDecimal(5))
            .andExpect(status().isOk());

        forceDeleteFee(dto.getCode());

    }

    @Test
    @Transactional
    public synchronized void testThatSameFeeOnceExpiredFeeIsNotFoundWhenLookup() throws Exception {

        RangedFeeDto dto = createDetailedFee();

        Fee fee = feeService.getFee(dto.getCode());

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        RangedFeeDto dto = createDetailedFee();

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setVersion(2);
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.ONE));
        versionDto.setStatus(FeeVersionStatusDto.pending_approval);
        versionDto.setDirection("licence");
        versionDto.setMemoLine("Hello");
        versionDto.setValidFrom(date);

        FeeVersion v = feeVersionService.save(dtoMapper.toFeeVersion(versionDto, "tarun"), dto.getCode());

        feeVersionService.changeStatus(dto.getCode(), v.getVersion(), FeeVersionStatus.approved, "tarun");

        assertTrue(sdf.format(date).equals(sdf.format(feeService.getFee(dto.getCode()).getCurrentVersion(true).getValidTo())));

        forceDeleteFee(dto.getCode());
    }

    @Test
    @Transactional
    public synchronized void testThatWhenANewVersionFutureDateIsApprovedTheCurrentOneIsMarkedToDatePreviousDayEndTime() throws Exception{
        RangedFeeDto dto = createDetailedFee();
        Date newVersionFromDate = createFutureDate(Calendar.getInstance(),new Date());
        Date expectedOldVersionToDate = createPreviousDate(Calendar.getInstance(),newVersionFromDate);

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setVersion(2);
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.ONE));
        versionDto.setStatus(FeeVersionStatusDto.pending_approval);
        versionDto.setDirection("licence");
        versionDto.setMemoLine("Hello");
        versionDto.setValidFrom(newVersionFromDate);

        FeeVersion v = feeVersionService.save(dtoMapper.toFeeVersion(versionDto, "testUser"), dto.getCode());
        FeeVersion currentVersion = v.getFee().getCurrentVersion(true);
        feeVersionService.changeStatus(dto.getCode(), v.getVersion(), FeeVersionStatus.approved, "testUser");
        assertTrue( expectedOldVersionToDate.compareTo(feeService.getFee(dto.getCode()).getCurrentVersion(true).getValidTo())==0);
        forceDeleteFee(dto.getCode());
    }

    @Test
    @Transactional
    public synchronized void testWhenNewVersionFutureFromDateAndToDateIsApprovedTheCurrentOneIsMarkedToDatePreviousDayEndTimeAndNewVersionToDateIsEOD() throws Exception{
        RangedFeeDto dto = createDetailedFee();
        Date newVersionFromDate = createFutureDate(Calendar.getInstance(),new Date());
        Date expectedOldVersionToDate = createPreviousDate(Calendar.getInstance(),newVersionFromDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setVersion(2);
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.ONE));
        versionDto.setStatus(FeeVersionStatusDto.pending_approval);
        versionDto.setDirection("licence");
        versionDto.setMemoLine("Hello");
        versionDto.setValidFrom(newVersionFromDate);
        versionDto.setValidTo (createFutureDate(Calendar.getInstance(),newVersionFromDate));

        FeeVersion v = feeVersionService.save(dtoMapper.toFeeVersion(versionDto, "testUser"), dto.getCode());
        feeVersionService.changeStatus(dto.getCode(), v.getVersion(), FeeVersionStatus.approved, "testUser");
        assertTrue(expectedOldVersionToDate.compareTo(feeService.getFee(dto.getCode()).getCurrentVersion(true).getValidTo())==0);
        assertTrue(v.getValidTo().compareTo(setEODTime(Calendar.getInstance(),versionDto.getValidTo()))==0);
        forceDeleteFee(dto.getCode());
    }

    @Test
    public synchronized void testThatWhenANewVersionIsApprovedAndTheCurrentOneIsMarkedForExpirationWhenItsLookedUpNewVersionIsFound() throws Exception {


        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        final RangedFeeDto dto =
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
                versionDto.setStatus(FeeVersionStatusDto.pending_approval);
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


    private RangedFeeDto createDetailedFee() {

        RangedFeeDto dto = new RangedFeeDto();

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
        versionDto.setStatus(FeeVersionStatusDto.approved);
        versionDto.setDirection("licence");
        versionDto.setMemoLine("Hello");
        versionDto.setValidFrom(new Date());

        dto.setVersion(versionDto);

        String code = feeService.save(dtoMapper.toFee(dto, AUTHOR)).getCode();

        dto.setCode(code);

        return dto;

    }

    private Date createFutureDate(Calendar calendar, Date date){
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 5);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    private Date createPreviousDate(Calendar calendar, Date date){
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return setEODTime(calendar,calendar.getTime());
    }
    private Date setEODTime(Calendar calendar, Date date){
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        calendar.add(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    @Test
    @Transactional
    public synchronized void testThatWhenReasonProvidedFeeRejected() throws Exception{

        Date date = new Date(System.currentTimeMillis() + 60000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        RangedFeeDto dto = createDetailedFee();

        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setVersion(2);
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.ONE));
        versionDto.setStatus(FeeVersionStatusDto.pending_approval);
        versionDto.setDirection("licence");
        versionDto.setMemoLine("Hello");
        versionDto.setValidFrom(date);

        FeeVersion v = feeVersionService.save(dtoMapper.toFeeVersion(versionDto, "sayali"), dto.getCode());

        feeVersionService.changeStatus(dto.getCode(), v.getVersion(), FeeVersionStatus.draft, "sayali", "wrong data");

        assertEquals(FeeVersionStatus.draft, v.getStatus());
        assertEquals("wrong data", v.getReasonForReject());
        assertEquals("sayali", v.getApprovedBy());

        forceDeleteFee(dto.getCode());
    }

    @Test
    @Transactional
    public void testFeeVersionsAreReturnedInDescendingOrder() {
        // Create FeeVersions with different versions
        Fee fee = new FixedFee();
        DateTime fromTime = new DateTime().minusDays(2);
        DateTime toTime = new DateTime().minusDays(1);
        FeeVersion feeVersion = new FeeVersion();
        feeVersion.setValidFrom(fromTime.toDate());
        feeVersion.setValidTo(toTime.toDate());
        feeVersion.setStatus(FeeVersionStatus.approved);
        feeVersion.setApprovedBy("Dave");
        feeVersion.setVersion(Integer.valueOf(1));
        feeVersion.setFee(fee);

        DateTime fromTime2 = new DateTime().minusDays(200);
        DateTime toTime2 = new DateTime().minusDays(100);
        FeeVersion feeVersion2 = new FeeVersion();
        feeVersion2.setValidFrom(fromTime2.toDate());
        feeVersion2.setValidTo(toTime2.toDate());
        feeVersion2.setStatus(FeeVersionStatus.approved);
        feeVersion2.setApprovedBy("Tim");
        feeVersion2.setVersion(Integer.valueOf(2));
        feeVersion2.setFee(fee);

        DateTime fromTime3 = new DateTime().minusDays(2000);
        DateTime toTime3 = new DateTime().minusDays(1000);
        FeeVersion feeVersion3 = new FeeVersion();
        feeVersion3.setValidFrom(fromTime.toDate());
        feeVersion3.setValidTo(toTime.toDate());
        feeVersion3.setStatus(FeeVersionStatus.approved);
        feeVersion3.setApprovedBy("Bob");
        feeVersion3.setVersion(Integer.valueOf(3));
        feeVersion3.setFee(fee);

        // Add FeeVersions to the Fee
        fee.setFeeVersions(new ArrayList<>(Arrays.asList(feeVersion, feeVersion2, feeVersion3)));

        // Save the Fee
        feeService.save(fee);

        // Create search criteria
        LookupFeeDto searchFeeCriteria = new LookupFeeDto();

        // Call searchFees
        List<Fee> result = feeService.search(searchFeeCriteria);

        // Verify that FeeVersions are sorted by version in descending order
        List<FeeVersion> feeVersions = result.get(0).getFeeVersions();

        // Assert that FeeVersions are sorted in descending order
        assertEquals("FeeVersions should be sorted in descending order", Integer.valueOf(3), feeVersions.get(0).getVersion());
        assertEquals("FeeVersions should be sorted in descending order", Integer.valueOf(2), feeVersions.get(1).getVersion());
        assertEquals("FeeVersions should be sorted in descending order", Integer.valueOf(1), feeVersions.get(2).getVersion());
    }

}
