package com.termproject.misaka.timesecretary.module;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.UUID;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateTimeCalendar;

/**
 * @author misaka
 */
public class Event extends Entity implements Comparable<Event> {

    public Event() {
        this(UUID.randomUUID());
    }

    public Event(UUID id) {
        super(id);
        Calendar mStartTime = cal2dateTimeCalendar(Calendar.getInstance());
        Calendar mEndTime = cal2dateTimeCalendar(Calendar.getInstance());
        if (Calendar.getInstance().get(Calendar.MINUTE) != 0) {
            mStartTime.set(Calendar.MINUTE, 0);
            mEndTime.set(Calendar.MINUTE, 0);
            mStartTime.add(Calendar.HOUR, 1);
            mEndTime.add(Calendar.HOUR, 2);
        } else {
            mEndTime.add(Calendar.HOUR, 1);
        }
        setStartTime(mStartTime);
        setEndTime(mEndTime);
    }

    @Override
    public int compareTo(@NonNull Event o) {
        if (getStartTime().equals(o.getStartTime())) {
            return getEndTime().before(o.getEndTime()) ? -1 : 1;
        } else {
            return getStartTime().before(o.getStartTime()) ? -1 : 1;
        }
    }
}
