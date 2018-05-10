package com.termproject.misaka.timesecretary.module;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.UUID;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateTimeCalendar;

/**
 * @author misaka
 */
public class Event implements Comparable<Event> {
    private UUID mId;
    private String mTitle;
    private String mNotes;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private UUID mCategory;

    public Event() {
        this(UUID.randomUUID());
    }

    public Event(UUID id) {
        mId = id;
        mStartTime = cal2dateTimeCalendar(Calendar.getInstance());
        mEndTime = cal2dateTimeCalendar(Calendar.getInstance());
        if (Calendar.getInstance().get(Calendar.MINUTE) != 0) {
            mStartTime.set(Calendar.MINUTE, 0);
            mEndTime.set(Calendar.MINUTE, 0);
            mStartTime.add(Calendar.HOUR, 1);
            mEndTime.add(Calendar.HOUR, 2);
        } else {
            mEndTime.add(Calendar.HOUR, 1);
        }
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Calendar getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Calendar startTime) {
        mStartTime = startTime;
    }

    public Calendar getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Calendar endTime) {
        mEndTime = endTime;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public UUID getCategory() {
        return mCategory;
    }

    public void setCategory(UUID category) {
        mCategory = category;
    }

    @Override
    public int compareTo(@NonNull Event o) {
        if (mStartTime.equals(o.mStartTime)) {
            return mEndTime.before(o.mEndTime) ? -1 : 1;
        } else {
            return mStartTime.before(o.mStartTime) ? -1 : 1;
        }
    }

    public long getDuration() {
        return mEndTime.getTimeInMillis() - mStartTime.getTimeInMillis();
    }
}
