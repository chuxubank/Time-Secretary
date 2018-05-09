package com.termproject.misaka.timesecretary.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.termproject.misaka.timesecretary.database.EventDbSchema.EventTable.Cols;
import com.termproject.misaka.timesecretary.module.Event;

import java.util.UUID;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.long2calendar;

public class EventCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public EventCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Event getEvent() {
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String uuidCategory = getString(getColumnIndex(Cols.CATEGORY));
        String title = getString(getColumnIndex(Cols.TITLE));
        String notes = getString(getColumnIndex(Cols.NOTES));
        long startTime = getLong(getColumnIndex(Cols.START_TIME));
        long endTime = getLong(getColumnIndex(Cols.END_TIME));

        Event event = new Event(UUID.fromString(uuidString));
        event.setCategory(UUID.fromString(uuidCategory));
        event.setTitle(title);
        event.setNotes(notes);
        event.setStartTime(long2calendar(startTime));
        event.setEndTime(long2calendar(endTime));
        return event;
    }
}
