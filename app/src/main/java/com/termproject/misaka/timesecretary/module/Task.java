package com.termproject.misaka.timesecretary.module;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.UUID;

/**
 * @author misaka
 */
public class Task implements Comparable<Task> {
    private UUID mId;
    private String mTitle;
    private String mNotes;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private Calendar mDeferUntil;
    private Calendar mDeadline;
    private UUID mCategory;
    private boolean mChecked;

    public Task() {
        this(UUID.randomUUID());
    }

    public Task(UUID id) {
        mId = id;
        mStartTime = Calendar.getInstance();
        mEndTime = Calendar.getInstance();
        mDeferUntil = Calendar.getInstance();
        mDeadline = Calendar.getInstance();
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

    public Calendar getDeferUntil() {
        return mDeferUntil;
    }

    public void setDeferUntil(Calendar deferUntil) {
        mDeferUntil = deferUntil;
    }

    public Calendar getDeadline() {
        return mDeadline;
    }

    public void setDeadline(Calendar deadline) {
        mDeadline = deadline;
    }

    public UUID getCategory() {
        return mCategory;
    }

    public void setCategory(UUID category) {
        mCategory = category;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    @Override
    public int compareTo(@NonNull Task o) {
        if (mDeferUntil.equals(o.mDeferUntil)) {
            return mDeadline.before(o.mDeadline) ? -1 : 1;
        } else {
            return mDeferUntil.before(o.mDeferUntil) ? -1 : 1;
        }
    }
}
