package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.CustomResultMatcher;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionStatusDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;


public class LookupFeeBorderTestCases extends BaseIntegrationTest {

    /* We need to add test cases like 299.98 claim and 299.99, 300, 300.01,
    and so on for all range based edges like at 500, 200000 etc
    Also as test case do add if someone makes a call with 299.98971 and 299.951
    */

    @Test
    @Transactional
    public void testBorderCase299_98() throws Exception{

        initFees();

        LookupFeeDto dto = new LookupFeeDto();
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("family");
        dto.setJurisdiction2("high court");
        dto.setAmountOrVolume(new BigDecimal("299.98"));

        // 299.98 * 5% = 14.999 -> 14.99
        double sum = 299.98 * 0.05;
        double result = (int)(sum * 100) / 100d;
        assertEquals("14.99", String.valueOf(result));

        lookupResultMatchesExpectedFeeAmount(new BigDecimal("14.99"));

        deleteFees();
    }

    @Test
    @Transactional
    public void testBorderCase299_99() throws Exception{

        initFees();

        LookupFeeDto dto = new LookupFeeDto();
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("family");
        dto.setJurisdiction2("high court");
        dto.setAmountOrVolume(new BigDecimal("299.99"));

        // 299.99 * 5% = 14.9995 -> 14.99
        double sum = 299.99 * 0.05;
        double result = (int)(sum * 100) / 100d;
        assertEquals("14.99", String.valueOf(result));

        lookupResultMatchesExpectedFeeAmount(new BigDecimal("14.99"));

        deleteFees();
    }

    @Test
    @Transactional
    public void testBorderCase300_00() throws Exception{

        initFees();

        LookupFeeDto dto = new LookupFeeDto();
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("family");
        dto.setJurisdiction2("high court");
        dto.setAmountOrVolume(new BigDecimal("300"));

        // 300 * 5% = 15
        double sum = 300 * 0.05;
        int result = (int)sum;
        assertEquals("15", String.valueOf(result));

        lookupResultMatchesExpectedFeeAmount(new BigDecimal("15"));

        deleteFees();
    }

    @Test
    @Transactional
    public void testBorderCase299_951() throws Exception{

        initFees();

        LookupFeeDto dto = new LookupFeeDto();
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("family");
        dto.setJurisdiction2("high court");
        dto.setAmountOrVolume(new BigDecimal("299.951"));

        // 299.951 * 5% = 14.99755 -> 14.99
        double sum = 299.951 * 0.05;
        double result = (int)(sum * 100) / 100d;
        assertEquals("14.99", String.valueOf(result));

        lookupResultMatchesExpectedFeeAmount(new BigDecimal("14.99"));

        deleteFees();
    }

    @Test
    @Transactional
    public void testBorderCase299_98971() throws Exception{

        initFees();

        LookupFeeDto dto = new LookupFeeDto();
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("family");
        dto.setJurisdiction2("high court");
        dto.setAmountOrVolume(new BigDecimal("299.98971"));

        // 299.98971 * 5% = 14.9994855 -> 14.99
        double sum = 299.98971 * 0.05;
        double result = (int)(sum * 100) / 100d;
        assertEquals("14.99", String.valueOf(result));

        lookupResultMatchesExpectedFeeAmount(new BigDecimal("14.99"));

        deleteFees();
    }

    @Test
    @Transactional
    public void testBorderCase500() throws Exception{

        initFees();

        LookupFeeDto dto = new LookupFeeDto();
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("family");
        dto.setJurisdiction2("high court");
        dto.setAmountOrVolume(new BigDecimal("500"));

        // 500 * 10% = 50
        double sum = 500 * 0.1;
        int result = (int)sum;
        assertEquals("50", String.valueOf(result));

        lookupResultMatchesExpectedFeeAmount(new BigDecimal("50"));

        deleteFees();
    }

    @Test
    @Transactional
    public void testBorderCase20000() throws Exception{

        initFees();

        LookupFeeDto dto = new LookupFeeDto();
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("family");
        dto.setJurisdiction2("high court");
        dto.setAmountOrVolume(new BigDecimal("20000"));

        // 20000 * 20% = 4000
        double sum = 20000 * 0.2;
        int result = (int)sum;
        assertEquals("4000", String.valueOf(result));

        lookupResultMatchesExpectedFeeAmount(new BigDecimal("4000"));

        deleteFees();
    }

    @Test
    @Transactional
    public void testBorderCase300_01() throws Exception{

        initFees();

        LookupFeeDto dto = new LookupFeeDto();
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("family");
        dto.setJurisdiction2("high court");
        dto.setAmountOrVolume(new BigDecimal("300.01"));

        // 300.01 * 10% = 30.001 -> 30
        double sum = 300.01 * 0.1;
        int result = (int)sum;
        assertEquals("30", String.valueOf(result));

        lookupResultMatchesExpectedFeeAmount(new BigDecimal("30"));

        deleteFees();
    }

    private void deleteFees() throws Exception{
        forceDeleteFee("T1");
        forceDeleteFee("T2");
        forceDeleteFee("T3");
    }

    /** Create 3 ranged-percent fees to test all border cases */
    private void initFees() throws Exception{
        createFee("T1", new BigDecimal("0.01"), new BigDecimal("300"), new BigDecimal("5"));
        createFee("T2", new BigDecimal("300.01"), new BigDecimal("500"), new BigDecimal("10"));
        createFee("T3", new BigDecimal("500.01"), null, new BigDecimal("20"));
    }

    protected void createFee(String code, BigDecimal minRange, BigDecimal maxRange, BigDecimal percent) throws Exception{

        FeeVersionDto version = new FeeVersionDto();
        version.setPercentageAmount(new PercentageAmountDto(percent));
        version.setStatus(FeeVersionStatusDto.approved);

        saveFeeAndCheckStatusIsCreated(new RangedFeeDto()
            .setService("divorce")
            .setEvent("issue")
            .setJurisdiction1("family")
            .setJurisdiction2("high court")
            .setCode(code)
            .setMinRange(minRange)
            .setMaxRange(maxRange)
            .setVersion(version));

    }


}
