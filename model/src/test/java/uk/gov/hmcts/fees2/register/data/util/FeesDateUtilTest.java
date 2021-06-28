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

import static org.assertj.core.api.Assertions.assertThat;

public class FeesDateUtilTest {

    Date date;

    @Before
    public void setUp() throws ParseException {
        final String startString = "January 10, 2021";
        final DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        date = format.parse(startString);
    }

    @Test
    public void testAddPreviousDateEODTime() {
        final Date date = FeesDateUtil.addPreviousDateEODTime(this.date);
        Assert.assertEquals("Sat Jan 09 23:59:59 GMT 2021", date.toString());
    }

    @Test
    public void testAddPreviousDateEODTimeNull() {
        final Date date = FeesDateUtil.addPreviousDateEODTime(null);
        Assert.assertNull(date);
    }

    @Test
    public void testAddEODTimeToDate() {
        final Date date = FeesDateUtil.addEODTimeToDate(this.date);
        Assert.assertEquals("Sun Jan 10 23:59:59 GMT 2021", date.toString());
    }

    @Test
    public void testAddEODTimeToDateNull() {
        final Date date = FeesDateUtil.addEODTimeToDate(null);
        Assert.assertNull(date);
    }

    @Test
    public void testDateToLocalDateTime() {
        final LocalDateTime localDateTime = FeesDateUtil.dateToLocalDateTime(date);
        assertThat(localDateTime.toString()).isEqualTo("2021-01-10T00:00");
    }

    @Test
    public void testDateToLocalDateTimeNull() {
        final LocalDateTime localDateTime = FeesDateUtil.dateToLocalDateTime(null);
        assertThat(localDateTime.toString()).isEqualTo("1970-01-01T01:00");
    }

    @Test
    public void testGetDateTimeForReportName() {
        final String reportName = FeesDateUtil.getDateTimeForReportName(date);
        assertThat(reportName).isEqualTo("100121_000000");
    }

    @Test
    public void testGetDateTimeForReportNameNull() {
        final String reportName = FeesDateUtil.getDateTimeForReportName(null);
        assertThat(reportName).isEqualTo("010170_010000");
    }

}
