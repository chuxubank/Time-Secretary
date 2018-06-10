package com.termproject.misaka.timesecretary.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.termproject.misaka.timesecretary.database.EventCursorWrapper;
import com.termproject.misaka.timesecretary.database.EventDbSchema.EventTable;
import com.termproject.misaka.timesecretary.database.LocalDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2day;

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

    public List<Event> getEventsByDay(int day) {
        List<Event> events = getEvents();
        List<Event> ans = new ArrayList<>();
        for (Event e : events) {
            if (cal2day(e.getStartTime()) == day) {
                ans.add(e);
            }
        }
        return ans;
    }


    public List<Event> getEventsByCategory(UUID categoryId) {
        List<Event> events = new ArrayList<>();
        EventCursorWrapper cursor = queryEvents(
                EventTable.Cols.CATEGORY + " = ?",
                new String[]{categoryId.toString()});
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

    public List<Event> getEvents(Calendar startDate, Calendar endDate) {
        List<Event> events = new ArrayList<>();
        EventCursorWrapper cursor = queryEvents(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Event e = cursor.getEvent();
                if ((!e.getStartTime().before(startDate) && e.getStartTime().before(endDate))
                        || e.getEndTime().after(startDate) && !e.getEndTime().after(endDate)) {
                    events.add(e);
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return events;
    }

    public List<Event> getUpcomingEvents() {
        List<Event> events = getEvents();
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event e = iterator.next();
            if (e.getStartTime().after(Calendar.getInstance())) {
                iterator.remove();
            }
        }
        return events;
    }

    public Event getUpcomingEvent() {
        List<Event> events = getUpcomingEvents();
        if (events.isEmpty() || events.get(0).getEndTime().before(Calendar.getInstance())) {
            return null;
        } else {
            Collections.sort(events);
            return events.get(0);
        }
    }

    public List<Event> queryEvents(String query) {
        List<Event> events = new ArrayList<>();
        EventCursorWrapper cursor = queryEvents(
                EventTable.Cols.TITLE + " like ? or " + EventTable.Cols.NOTES + " like ?",
                new String[]{'%' + query + '%', '%' + query + '%'});
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
