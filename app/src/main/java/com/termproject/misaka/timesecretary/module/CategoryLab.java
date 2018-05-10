package com.termproject.misaka.timesecretary.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.termproject.misaka.timesecretary.database.CategoryCursorWrapper;
import com.termproject.misaka.timesecretary.database.CategoryDbSchema.CategoryTable;
import com.termproject.misaka.timesecretary.database.LocalDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * @author misaka
 */
public class CategoryLab {
    private static final String TAG = "CategoryLab";
    private static CategoryLab sCategoryLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CategoryLab get(Context context) {
        if (sCategoryLab == null) {
            sCategoryLab = new CategoryLab(context);
        }
        return sCategoryLab;
    }

    private CategoryLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LocalDbHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(Category category) {
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.UUID, category.getId().toString());
        values.put(CategoryTable.Cols.TITLE, category.getTitle());
        values.put(CategoryTable.Cols.COLOR, category.getColor());
        return values;
    }

    public void addCategory(Category e) {
        ContentValues values = getContentValues(e);
        mDatabase.insert(CategoryTable.NAME, null, values);
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        CategoryCursorWrapper cursor = queryCategories(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                categories.add(cursor.getCategory());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return categories;
    }

    public Category getCategory(UUID id) {
        CategoryCursorWrapper cursor = queryCategories(
                CategoryTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCategory();
        } finally {
            cursor.close();
        }
    }

    public void updateCategory(Category category) {
        String uuidString = category.getId().toString();
        ContentValues values = getContentValues(category);
        mDatabase.update(CategoryTable.NAME, values,
                CategoryTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void deleteCategory(Category category) {
        String uuidString = category.getId().toString();
        mDatabase.delete(CategoryTable.NAME,
                CategoryTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void clearNoTitle() {
        mDatabase.delete(CategoryTable.NAME,
                CategoryTable.Cols.TITLE + " is ?",
                null);
    }

    private CategoryCursorWrapper queryCategories(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CategoryTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CategoryCursorWrapper(cursor);
    }

    public long getDuration(UUID categoryId) {
        long duration = 0;
        List<Event> events = EventLab.get(mContext).getEvents();
        List<Task> tasks = TaskLab.get(mContext).getTasks();
        for (Event e : events) {
            if (e.getCategory().equals(categoryId)) {
                duration += e.getDuration();
            }
        }
        for (Task t : tasks) {
            if (t.getCategory().equals(categoryId)) {
                duration += t.getDuration();
            }
        }
        return duration;
    }

    public long getDuration(UUID categoryId, Calendar startDate, Calendar endDate) {
        long duration = 0;
        List<Event> events = EventLab.get(mContext).getEvents();
        List<Task> tasks = TaskLab.get(mContext).getTasks();
        for (Event e : events) {
            if (e.getCategory().equals(categoryId) && !e.getStartTime().before(startDate) && !e.getEndTime().after(endDate)) {
                Log.i(TAG, e.getDuration() + "");
                duration += e.getDuration();
            }
        }
        for (Task t : tasks) {
            if (t.getCategory().equals(categoryId) && !t.getStartTime().before(startDate) && !t.getEndTime().after(endDate)) {
                duration += t.getDuration();
            }
        }
        return duration;
    }
}
