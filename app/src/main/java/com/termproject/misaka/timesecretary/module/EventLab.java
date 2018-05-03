package com.termproject.misaka.timesecretary.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.termproject.misaka.timesecretary.database.EventCursorWrapper;
import com.termproject.misaka.timesecretary.database.EventDbSchema.EventTable;
import com.termproject.misaka.timesecretary.database.LocalDbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author misaka
 */
public class EventLab {
    private static EventLab sEventLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static EventLab get(Context context) {
        if (sEventLab == null) {
            sEventLab = new EventLab(context);
        }
        return sEventLab;
    }

    private EventLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LocalDbHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(Event event) {
        ContentValues values = new ContentValues();
        values.put(EventTable.Cols.UUID, event.getId().toString());
        values.put(EventTable.Cols.CATEGORY, event.getCategory().toString());
        values.put(EventTable.Cols.TITLE, event.getTitle());
        values.put(EventTable.Cols.NOTES, event.getNotes());
        values.put(EventTable.Cols.START_TIME, event.getStartTime().getTime().getTime());
        values.put(EventTable.Cols.END_TIME, event.getEndTime().getTime().getTime());
        return values;
    }

    public void addEvent(Event e) {
        ContentValues values = getContentValues(e);
        mDatabase.insert(EventTable.NAME, null, values);
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        EventCursorWrapper cursor = queryEvents(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                events.add(cursor.getEvent());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return events;
    }

    public Event getEvent(UUID id) {
        EventCursorWrapper cursor = queryEvents(
                EventTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getEvent();
        } finally {
            cursor.close();
        }
    }

    public void updateEvent(Event event) {
        String uuidString = event.getId().toString();
        ContentValues values = getContentValues(event);
        mDatabase.update(EventTable.NAME, values,
                EventTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void deleteEvent(Event event) {
        String uuidString = event.getId().toString();
        mDatabase.delete(EventTable.NAME,
                EventTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void clearNoTitle() {
        mDatabase.delete(EventTable.NAME,
                EventTable.Cols.TITLE + " is ?",
                null);
    }

    private EventCursorWrapper queryEvents(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                EventTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new EventCursorWrapper(cursor);
    }
}
