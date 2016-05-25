package au.com.websitemasters.schools.thornlie.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ruslan on 24.03.2016.
 */
public class DateStringGetter {

    public Date convertStringToDate(String dateInString){

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public String getDay(String date){
        String day = "";
        day = date.substring(0, 2);
        return day;
    }

    public String getMonth(String date){
        String month = "";
        month = date.substring(3, 5);
        return month;
    }

    public String getYear(String date){
        String year = "";
        year = date.substring(6, 10);
        return year;
    }

    //public String getHour(String date){
    //    String year = "";
    //    year = date.substring(11, 16);
    //    return year;
    //}


    public String getWithoutHour(String date){
        String year = "";
        year = date.substring(0, 10);
        return year;
    }
}
