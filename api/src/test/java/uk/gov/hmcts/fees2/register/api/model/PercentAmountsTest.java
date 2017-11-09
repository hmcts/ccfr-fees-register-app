package uk.gov.hmcts.fees2.register.api.model;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.data.model.amount.Amount;
import uk.gov.hmcts.fees2.register.data.model.amount.PercentageAmount;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PercentAmountsTest {

    @Test
    public void simple() {
        Amount amount = new PercentageAmount(new BigDecimal("0.1"));
        assertTrue(amount.calculateFee(BigDecimal.TEN).compareTo(BigDecimal.ONE) == 0);
    }

    @Test
    public void zero() {
        Amount amount = new PercentageAmount(new BigDecimal("0.1"));
        assertTrue(amount.calculateFee(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    public void typical() {
        Amount amount = new PercentageAmount(new BigDecimal("0.05"));
        assertTrue(amount.calculateFee(new BigDecimal("65")).compareTo(new BigDecimal("3.25")) == 0);
    }

    /* EX-50:
        Civil court fees -> Issuing claims -> Starting your claim -> Money claims
        – fees order 1.1-1.2
        To issue a claim for money, the fees are based on the amount claimed, including interest.
        For Court Issued Claims, please round fractions of pence down to the nearest penny.
        Example: A Fee calculated as being £1050.5096 rounds down to a payable fee of £1050.50
    */

    @Test
    public void truncate() {
        Amount amount = new PercentageAmount(new BigDecimal("0.0001"));
        assertEquals(new BigDecimal("1050.50"), amount.calculateFee(new BigDecimal("10505096")));
    }


}
