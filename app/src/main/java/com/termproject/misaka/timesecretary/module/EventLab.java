package com.termproject.misaka.timesecretary.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

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

    private EventLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new EventBaseHelper(mContext).getWritableDatabase();
        mEvents = new ArrayList<>();
    }

    public void addEvent(Event e) {
        mEvents.add(e);
    }

    public void deleteEvent(Event e) {
        mEvents.remove(e);
    }

    public void clearNoTitle() {
        for (Event e : mEvents) {
            if (TextUtils.isEmpty(e.getTitle())) {
                mEvents.remove(e);
            }
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
