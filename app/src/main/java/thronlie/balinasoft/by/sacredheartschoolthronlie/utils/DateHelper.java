package thronlie.balinasoft.by.sacredheartschoolthronlie.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ruslan on 07.04.2016.
 */
public class DateHelper {

    //1209600000 secondsinto 2 weeks

    public DateHelper(){
    }

    public String getCurrentDate_Format(String DMY){
        DateFormat dateFormat = new SimpleDateFormat(DMY);
        String currentDate = dateFormat.format(new Date());
        return currentDate;
    }

    public long getCurrentDate_Seconds(){
        long seconds = (new Date().getTime())/1000;
        return seconds;
    }

    public long getCurrentDateMinusTwoWeeks_Seconds(){
        long seconds = ((new Date().getTime())- 1209600000)/1000;
        return seconds;
    }

    public long getCurrentDateMinusFourWeeks_Seconds(){
        long seconds = ((new Date().getTime())- 1209600000 - 1209600000)/1000;
        return seconds;
    }

    public String convertSecondsToDate(long seconds, String DMY){
        String dateString = new SimpleDateFormat(DMY, Locale.US)
                .format(new Date(seconds * 1000));
        return dateString;
    }

    public String convertSecondsToDate(String seconds, String DMY){

        String dateString = new SimpleDateFormat(DMY, Locale.US)
                .format(new Date(Long.parseLong(seconds) * 1000));

        return dateString;
    }


}
