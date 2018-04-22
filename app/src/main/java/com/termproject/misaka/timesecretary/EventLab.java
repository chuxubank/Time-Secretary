package com.termproject.misaka.timesecretary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.termproject.misaka.timesecretary.database.EventBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author misaka
 */
public class EventLab {
    private static EventLab sEventLab;
    private List<Event> mEvents;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private int mSize = 3;

    private EventLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new EventBaseHelper(mContext).getWritableDatabase();
        mEvents = new ArrayList<>();
        for (int i = 0; i < mSize; i++) {
            Event event = new Event();
            event.setTitle("Event #" + i);
            mEvents.add(event);
        }

    }

    public static EventLab get(Context context) {
        if (sEventLab == null) {
            sEventLab = new EventLab(context);
        }
        return sEventLab;
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public Event getEvent(UUID id) {
        for (Event event : mEvents) {
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }
}
