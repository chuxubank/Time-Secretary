package com.termproject.misaka.timesecretary.utils;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * @author misaka
 */
public final class TimeUtils {
    private static final DateFormat DEFAULT_DATE_FORMAT = DateFormat.getDateInstance();
    private static final DateFormat DEFAULT_TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);

    public static String cal2dateString(final Calendar cal) {
        return DEFAULT_DATE_FORMAT.format(cal.getTime());
    }

    public static String cal2longDateString(final Calendar cal) {
        return DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
    }

    public static String cal2timeString(final Calendar cal) {
        return DEFAULT_TIME_FORMAT.format(cal.getTime());
    }

    public static Calendar long2calendar(final long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    public static long cal2long(final Calendar cal) {
        return cal.getTimeInMillis();
    }

    public static int cal2day(final Calendar cal) {
        return cal.get(Calendar.YEAR) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
    }
}
