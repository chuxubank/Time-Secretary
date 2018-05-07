package com.termproject.misaka.timesecretary.module;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.UUID;

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
        mStartTime = Calendar.getInstance();
        mStartTime.set(Calendar.MINUTE, 0);
        mStartTime.set(Calendar.SECOND, 0);
        mStartTime.add(Calendar.HOUR, 1);
        mEndTime = Calendar.getInstance();
        mEndTime.set(Calendar.MINUTE, 0);
        mEndTime.set(Calendar.SECOND, 0);
        mEndTime.add(Calendar.HOUR, 2);
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
}
