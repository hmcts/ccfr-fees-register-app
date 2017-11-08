package uk.gov.hmcts.fees2.register.data;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;

import java.util.Date;

import static org.junit.Assert.*;


public class FeeVersionTest {

    @Test
    public void testRange() throws Exception{

        FeeVersion v = new FeeVersion();

        v.setValidFrom(new Date(System.currentTimeMillis()));

        Thread.sleep(50);

        Date d = new Date();

        assertTrue(v.isInRange(d));

        Thread.sleep(50);

        v.setValidTo(new Date());

        assertTrue(v.isInRange(d));

        Thread.sleep(50);


        assertFalse(v.isInRange(new Date()));

    }

}
