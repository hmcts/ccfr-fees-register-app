package uk.gov.hmcts.fees2.register.data.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class FeesDateUtil {

    private static final int DAYOFYEAR = -1;
    private static final int STARTMINUTE = 0;
    private static final int STARTSECOND = 0;
    private static final int EODHOURS = 23;
    private static final int EODMINUTE = 59;
    private static final int EODSECOND = 59;

    public static Date addPreviousDateEODTime(final Date dateReceived) {
        if (dateReceived != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateReceived);
            calendar.add(Calendar.DAY_OF_YEAR, DAYOFYEAR);
            return addEODTimeToDate(calendar.getTime());
        } else {
            return null;
        }
    }

    public static Date addEODTimeToDate(final Date toDateReceived) {
        if (toDateReceived != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(toDateReceived);
            calendar.set(Calendar.MINUTE, STARTMINUTE);
            calendar.set(Calendar.SECOND, STARTSECOND);
            calendar.add(Calendar.HOUR_OF_DAY, EODHOURS);
            calendar.add(Calendar.MINUTE, EODMINUTE);
            calendar.add(Calendar.SECOND, EODSECOND);
            return calendar.getTime();

        } else {
            return null;
        }
    }

    public static LocalDateTime dateToLocalDateTime(final Date date) {
        return date == null ? LocalDateTime.ofInstant(
                new Date(0).toInstant(),
                ZoneId.systemDefault()
        ) : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static String getDateTimeForReportName(final Date date) {
        final DateTimeFormatter reportNameDateFormat = DateTimeFormatter.ofPattern("ddMMyy_HHmmss");
        return dateToLocalDateTime(date).format(reportNameDateFormat);
    }
}
