package com.termproject.misaka.timesecretary;

import java.util.Calendar;
import java.util.UUID;

/**
 * @author misaka
 */
public class Task {
    private UUID mId;
    private String mTitle;
    private String mNotes;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private Calendar mDeadline;
    private String mCategory;
    private boolean mAccomplished;

    public Task() {
        mId = UUID.randomUUID();
        mStartTime = Calendar.getInstance();

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

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
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

    public Calendar getDeadline() {
        return mDeadline;
    }

    public void setDeadline(Calendar deadline) {
        mDeadline = deadline;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public boolean isAccomplished() {
        return mAccomplished;
    }

    public void setAccomplished(boolean accomplished) {
        mAccomplished = accomplished;
    }
}
