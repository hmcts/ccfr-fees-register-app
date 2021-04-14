package uk.gov.hmcts.fees2.register.data.util;

import java.util.Calendar;
import java.util.Date;

public class FeesDateUtil {

    private static int DAYOFYEAR = -1;
    private static int STARTMINUTE = 0;
    private static int STARTSECOND = 0;
    private static int EODHOURS = 23;
    private static int EODMINUTE = 59;
    private static int EODSECOND = 59;

    public static Date addEODTime(Date dateReceived) {
        if (dateReceived != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateReceived);
            calendar.add(Calendar.DAY_OF_YEAR, DAYOFYEAR);
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

    public static Date addEODTimeToDate(Date toDateReceived) {
        if (toDateReceived != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(toDateReceived);
            calendar.add(Calendar.HOUR_OF_DAY, EODHOURS);
            calendar.add(Calendar.MINUTE, EODMINUTE);
            calendar.add(Calendar.SECOND, EODSECOND);
            return calendar.getTime();
        } else {
            return null;
        }
    }
}
