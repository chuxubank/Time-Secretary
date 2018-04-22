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

    public static String cal2timeString(final Calendar cal) {
        return DEFAULT_TIME_FORMAT.format(cal.getTime());
    }

}
