package www.codycarlton.utils;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StringTimeUtils {

    public static String currentDate(){
        return java.text.DateFormat.
                getDateInstance().format(Calendar.getInstance().getTime());
    }

    public static String currentTime(){
        return java.text.DateFormat.
                getTimeInstance().format(Calendar.getInstance().getTime());
    }

    public static String currentZonedTime(){
        return ZonedDateTime.now().toString();
    }

    public static String totalHoursTime(long totalTimeMillis){

        /*
         * Read stored string ZonedDateTime
         * Convert back to ZonedDateTime once read
         * continue operations as normal
         * */

        long hr = TimeUnit.MILLISECONDS.toHours(totalTimeMillis);
        long min = TimeUnit.MILLISECONDS.toMinutes(totalTimeMillis)%60;
        long sec = TimeUnit.MILLISECONDS.toSeconds(totalTimeMillis)%60;

        return String.format(Locale.getDefault(),"%02d:%02d:%02d", hr, min, sec );

    }

}
