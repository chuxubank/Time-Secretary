package com.termproject.misaka.timesecretary.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.termproject.misaka.timesecretary.database.TaskDbSchema.TaskTable.Cols;
import com.termproject.misaka.timesecretary.module.Task;

import java.util.UUID;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.long2calendar;

public class TaskCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String uuidCategory = getString(getColumnIndex(Cols.CATEGORY));
        String title = getString(getColumnIndex(Cols.TITLE));
        String notes = getString(getColumnIndex(Cols.NOTES));
        boolean checked = Boolean.parseBoolean(getString(getColumnIndex(Cols.CHECKED)));
        long startTime = getLong(getColumnIndex(Cols.START_TIME));
        long endTime = getLong(getColumnIndex(Cols.END_TIME));
        long deferUntil = getLong(getColumnIndex(Cols.DEFER_UNTIL));
        long deadline = getLong(getColumnIndex(Cols.DEADLINE));

        Task task = new Task(UUID.fromString(uuidString));
        task.setCategory(UUID.fromString(uuidCategory));
        task.setTitle(title);
        task.setNotes(notes);
        task.setStartTime(long2calendar(startTime));
        task.setEndTime(long2calendar(endTime));
        task.setDeferUntil(long2calendar(deferUntil));
        task.setDeadline(long2calendar(deadline));
        task.setChecked(checked);
        return task;
    }
}
