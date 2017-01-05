package com.google.developer.udacityalumni.utility;

import android.content.Context;

import com.google.developer.udacityalumni.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public final class Date_Utils {

    private static final String LOG_TAG = Date_Utils.class.getSimpleName();

    public static Long getTimeInMillis(String dateJson) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateJson.substring(0, 10) + " " + dateJson.substring(11, 19);
        try {
            Date date = dateFormatter.parse(formattedDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatTimeAgo(Context context, long date) {
        long timeDiffMillis = Calendar.getInstance().getTimeInMillis() - date;
        if (timeDiffMillis < TimeUnit.MINUTES.toMillis(1))
            return context.getString(R.string.seconds_ago, TimeUnit.MILLISECONDS.toSeconds(timeDiffMillis));
        if (timeDiffMillis < TimeUnit.HOURS.toMillis(1))
            return context.getString(R.string.minutes_ago, TimeUnit.MILLISECONDS.toMinutes(timeDiffMillis));
        if (timeDiffMillis < TimeUnit.DAYS.toMillis(1))
            return context.getString(R.string.hours_ago, TimeUnit.MILLISECONDS.toHours(timeDiffMillis));
        if (timeDiffMillis < TimeUnit.DAYS.toMillis(7))
            return context.getString(R.string.days_ago, TimeUnit.MILLISECONDS.toDays(timeDiffMillis));
        if (timeDiffMillis < TimeUnit.DAYS.toMillis(31))
            return context.getString(R.string.weeks_ago, TimeUnit.MILLISECONDS.toDays(timeDiffMillis)/7L);
        if (timeDiffMillis < TimeUnit.DAYS.toMillis(365))
            return context.getString(R.string.months_ago, TimeUnit.MILLISECONDS.toDays(timeDiffMillis)/30L);
        return context.getString(R.string.years_ago, TimeUnit.MILLISECONDS.toDays(timeDiffMillis) / 365L);
    }

    public static String fetch(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = new OkHttpClient().newCall(request).execute()) {
            return response.body().string();
        }
    }
}
