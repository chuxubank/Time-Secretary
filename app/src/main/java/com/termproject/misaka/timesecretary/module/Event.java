package com.termproject.misaka.timesecretary.module;

import java.util.Calendar;
import java.util.UUID;

/**
 * @author misaka
 */
public class Event {
    private UUID mId;
    private String mTitle;
    private String mNotes;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private UUID mCategory;

    public Event() {
        mId = UUID.randomUUID();
        mStartTime = Calendar.getInstance();
        mEndTime = Calendar.getInstance();
        mEndTime.add(Calendar.HOUR, 1);
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
}
