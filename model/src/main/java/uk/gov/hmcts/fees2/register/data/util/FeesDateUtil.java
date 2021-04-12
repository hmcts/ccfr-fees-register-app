package uk.gov.hmcts.fees2.register.data.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FeesDateUtil {

    private static int DAY_OF_YEAR = -1;
    private static int START_MINUTE = 0;
    private static int START_SECOND = 0;
    private static int END_HOURS = 23;
    private static int END_MINUTE = 59;
    private static int END_SECOND = 59;

    public static Date addEODTime(Date dateReceived) {
        if (dateReceived != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateReceived);
            calendar.add(Calendar.DAY_OF_YEAR, DAY_OF_YEAR);
            //calendar.set(Calendar.HOUR, 1);
            calendar.set(Calendar.MINUTE, START_MINUTE);
            calendar.set(Calendar.SECOND, START_SECOND);

            calendar.add(Calendar.HOUR_OF_DAY, END_HOURS);
            calendar.add(Calendar.MINUTE, END_MINUTE);
            calendar.add(Calendar.SECOND, END_SECOND);

            return calendar.getTime();
        } else {
            return null;
        }
    }

    public static Date addEODTimeToDate(Date toDateReceived) {
        if (toDateReceived != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(toDateReceived);
            calendar.add(Calendar.HOUR_OF_DAY, END_HOURS);
            calendar.add(Calendar.MINUTE, END_MINUTE);
            calendar.add(Calendar.SECOND, END_SECOND);
            return calendar.getTime();
        } else {
            return null;
        }
    }
}
