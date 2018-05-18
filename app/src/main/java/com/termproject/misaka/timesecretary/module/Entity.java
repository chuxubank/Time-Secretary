package com.termproject.misaka.timesecretary.module;

import java.util.Calendar;
import java.util.UUID;

public abstract class Entity {
    private UUID mId;
    private String mTitle;
    private String mNotes;
    private Calendar startTime;
    private Calendar mEndTime;
    private UUID mCategory;

    public Entity(UUID id) {
        mId = id;
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
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Calendar endTime) {
        mEndTime = endTime;
    }

    public UUID getCategory() {
        return mCategory;
    }

    public void setCategory(UUID category) {
        mCategory = category;
    }

    public long getDuration() {
        return mEndTime.getTimeInMillis() - startTime.getTimeInMillis();
    }
}