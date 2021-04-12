package uk.gov.hmcts.fees2.register.data.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FeesDateUtil {

    public static Date addEODTime(Date dateReceived) {
        if (dateReceived != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateReceived);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            calendar.set(Calendar.HOUR, 1);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            calendar.add(Calendar.HOUR_OF_DAY, 23);
            calendar.add(Calendar.MINUTE, 59);
            calendar.add(Calendar.SECOND, 59);

            return calendar.getTime();
        } else {
            return null;
        }
    }
}
