package com.termproject.misaka.timesecretary.utils;

import com.termproject.misaka.timesecretary.module.Entity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

    public static Calendar cal2dateCalendar(final Calendar cal) {
        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    public static Calendar cal2dateTimeCalendar(final Calendar cal) {
        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
    }

    public static long cal2long(final Calendar cal) {
        return cal.getTimeInMillis();
    }

    public static int cal2day(final Calendar cal) {
        return cal.get(Calendar.YEAR) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
    }

    public static String diffOfDay(final Calendar cal) {
        int today = cal2day(Calendar.getInstance());
        int c = cal2day(cal);
        int diff = c - today;
        if (diff >= 0) {
            return diff > 1 ? diff + " days left" : diff + " day left";
        } else {
            return diff < -1 ? -diff + " days ago" : -diff + " day ago";
        }
    }

    public static long diffOfTime(final Calendar cal) {
        return cal2long(cal) - cal2long(Calendar.getInstance());
    }

    public static long getDuration(final Entity e, final Calendar start, final Calendar end) {
        return min(end.getTimeInMillis(), e.getEndTime().getTimeInMillis()) - max(start.getTimeInMillis(), e.getStartTime().getTimeInMillis());
    }

    public static long max(final long a, final long b) {
        return a > b ? a : b;
    }

    public static long min(final long a, final long b) {
        return a < b ? a : b;
    }
}
