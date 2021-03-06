package com.msurvey.projectm.msurveyaod.Utilities;

import android.util.Log;

import org.joda.time.Years;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    //Days

    public static String Monday = "Monday";
    public static String Tuesday = "Tuesday";
    public static String Wednesday = "Wednesday";
    public static String Thursday = "Thursday";
    public static String Friday = "Friday";
    public static String Saturday = "Saturday";
    public static String Sundday = "Sunday";

    //Months
    public static String January = "Jan";
    public static String February = "Feb";
    public static String March = "Mar";
    public static String April = "Apr";
    public static String May = "May";
    public static String June = "Jun";
    public static String July = "Jul";
    public static String August = "Aug";
    public static String September = "Sep";
    public static String October = "Oct";
    public static String November = "Nov";
    public static String December = "Dec";

    public static String returnDate(String date) throws ParseException {

        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);

        Date newDate = parser.parse(date);

        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

        //Format to different date format

        date =newFormat.format(newDate);

        return date;
    }

    public static String returnTransactionDate(String date) throws ParseException{

        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);

        Date newDate = parser.parse(date);

        SimpleDateFormat newFormat = new SimpleDateFormat("EEE dd MMM yyyy", Locale.ENGLISH);

        //Format to different date format

        date =newFormat.format(newDate);

        return date;

    }


    public static String returnFormalDate(String date, String time) throws ParseException {

        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);

        Date newDate = parser.parse(date);

        SimpleDateFormat newFormat = new SimpleDateFormat("EEE dd MMM yyyy", Locale.ENGLISH);

        //Format to different date format

        date =newFormat.format(newDate);

        return date + ". " + time;
    }

    public static String returnTimestamp(String date, String time) throws ParseException{

    //To be written

        return "";

    }

    public static String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
