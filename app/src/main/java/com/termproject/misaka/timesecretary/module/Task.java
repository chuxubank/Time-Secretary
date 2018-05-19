package com.termproject.misaka.timesecretary.module;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.UUID;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateCalendar;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.long2calendar;

/**
 * @author misaka
 */
public class Task extends Entity implements Comparable<Task> {

    private Calendar mDeferUntil;
    private Calendar mDeadline;
    private boolean mChecked;

    public Task() {
        this(UUID.randomUUID());
    }

    public Task(UUID id) {
        super(id);
        setStartTime(long2calendar(0));
        setEndTime(long2calendar(0));
        mDeferUntil = cal2dateCalendar(Calendar.getInstance());
        mDeadline = cal2dateCalendar(Calendar.getInstance());
        mChecked = false;
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
