package com.google.developer.udacityalumni.util;

import java.util.Calendar;

/**
 * Created by benjaminlewis on 1/3/17.
 */

public final class Utility {

    public static long getTimeInMillis(String date){
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8,10));
        int hour = Integer.parseInt(date.substring(11,13));
        int minute = Integer.parseInt(date.substring(14,16));
        int second = Integer.parseInt(date.substring(17,19));
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute, second);
        return cal.getTimeInMillis();
    }
}
