package uk.gov.hmcts.fees2.register.data.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class FeesDateUtilTest {

    Date date;

    @Before
    public void setUp() throws ParseException {
        final String startString = "January 10, 2021";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        date = format.parse(startString);
    }

    @Test
    public void testAddPreviousDateEODTime() {
        final Date date = FeesDateUtil.addPreviousDateEODTime(this.date);
        Assert.assertTrue(date.toString().matches("Sat Jan 09 23:59:59 (UTC|GMT) 2021"));
    }

    @Test
    public void testAddPreviousDateEODTimeNull() {
        final Date date = FeesDateUtil.addPreviousDateEODTime(null);
        Assert.assertNull(date);
    }

    @Test
    public void testAddEODTimeToDate() {
        final Date date = FeesDateUtil.addEODTimeToDate(this.date);
        Assert.assertTrue(date.toString().matches("Sun Jan 10 23:59:59 (UTC|GMT) 2021"));
    }

    @Test
    public void testAddEODTimeToDateNull() {
        final Date date = FeesDateUtil.addEODTimeToDate(null);
        Assert.assertNull(date);
    }

    @Test
    public void testDateToLocalDateTime() {
        final LocalDateTime localDateTime = FeesDateUtil.dateToLocalDateTime(date);
        Assert.assertTrue(localDateTime.toString().matches("2021-01-10T0(0|1):00"));
    }

    @Test
    public void testDateToLocalDateTimeNull() {
        final LocalDateTime localDateTime = FeesDateUtil.dateToLocalDateTime(null);
        Assert.assertTrue(localDateTime.toString().matches("1970-01-01T0(0|1):00"));
    }

}
