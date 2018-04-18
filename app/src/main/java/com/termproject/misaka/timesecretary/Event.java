package com.termproject.misaka.timesecretary;

import java.util.Calendar;
import java.util.UUID;

public class Event {
    private UUID mId;
    private String mTitle;
    private String mDescription;
    private boolean mAsTask;
    private boolean mCompleted;
    private Calendar mStartDate;
    private Calendar mEndDate;

    public Event() {
        mId = UUID.randomUUID();
        mStartDate = Calendar.getInstance();
        mEndDate = Calendar.getInstance();
        mEndDate.add(Calendar.HOUR, 1);
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

    public Calendar getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Calendar startDate) {
        mStartDate = startDate;
    }

    public Calendar getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Calendar endDate) {
        mEndDate = endDate;
    }

    public boolean isTask() {
        return mAsTask;
    }

    public void setAsTask(boolean asTask) {
        mAsTask = asTask;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
