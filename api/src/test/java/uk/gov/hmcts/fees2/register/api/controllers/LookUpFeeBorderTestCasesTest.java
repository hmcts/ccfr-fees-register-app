package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionStatusDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;

import java.math.BigDecimal;

public class LookUpFeeBorderTestCasesTest extends BaseIntegrationTest {

    /* We need to add test cases like 299.98 claim and 299.99, 300, 300.01,
    and so on for all range based edges like at 500, 200000 etc
    Also as test case do add if someone makes a call with 299.98971 and 299.951
    */

    @Test
    @Transactional
    public void testBorderCase299_98() throws Exception{

        assertNotNull(testBorderCase(new BigDecimal("299.98"), new BigDecimal("14.99")));

    }

    @Test
    @Transactional
    public void testBorderCase299_99() throws Exception{

        assertNotNull(testBorderCase(new BigDecimal("299.99"), new BigDecimal("14.99")));

    }

    @Test
    @Transactional
    public void testBorderCase300_00() throws Exception{

        assertNotNull(testBorderCase(new BigDecimal("300"), new BigDecimal("15")));

    }

    @Test
    @Transactional
    public void testBorderCase299_951() throws Exception{

        assertNotNull(testBorderCase(new BigDecimal("299.951"), new BigDecimal("14.99")));

    }

    @Test
    @Transactional
    public void testBorderCase299_98971() throws Exception{

        assertNotNull(testBorderCase(new BigDecimal("299.98971"), new BigDecimal("14.99")));

    }

    @Test
    @Transactional
    public void testBorderCase500() throws Exception{

        assertNotNull(testBorderCase(new BigDecimal("500"), new BigDecimal("50")));

    }

    @Test
    @Transactional
    public void testBorderCase20000() throws Exception{

        assertNotNull(testBorderCase(new BigDecimal("20000"), new BigDecimal("4000")));

    }

    @Test
    @Transactional
    public void testBorderCase300_01() throws Exception{

        assertNotNull(testBorderCase(new BigDecimal("300.01"), new BigDecimal("30")));

    }

    public ResultMatcher testBorderCase(BigDecimal amountOrVolume, BigDecimal feeAmount) throws Exception{

        initFees();

        LookupFeeDto dto = new LookupFeeDto();
        dto.setService("divorce");
        dto.setEvent("issue");
        dto.setJurisdiction1("family");
        dto.setJurisdiction2("high court");
        dto.setAmountOrVolume(amountOrVolume);

        // 300.01 * 10% = 30.001 -> 30

        ResultMatcher resultMatcher = lookupResultMatchesExpectedFeeAmount(feeAmount);

        deleteFees();
        return resultMatcher;
    }

    private void deleteFees() {
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