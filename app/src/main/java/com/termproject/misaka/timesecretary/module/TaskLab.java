package com.termproject.misaka.timesecretary.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.termproject.misaka.timesecretary.database.LocalDbHelper;
import com.termproject.misaka.timesecretary.database.TaskCursorWrapper;
import com.termproject.misaka.timesecretary.database.TaskDbSchema.TaskTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateCalendar;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2day;

/**
 * @author misaka
 */
public class TaskLab {
    private static TaskLab sTaskLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static final String TAG = "TaskLab";

    public static TaskLab get(Context context) {
        if (sTaskLab == null) {
            sTaskLab = new TaskLab(context);
        }
        return sTaskLab;
    }

    private TaskLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LocalDbHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskTable.Cols.UUID, task.getId().toString());
        values.put(TaskTable.Cols.CATEGORY, task.getCategory().toString());
        values.put(TaskTable.Cols.TITLE, task.getTitle());
        values.put(TaskTable.Cols.NOTES, task.getNotes());
        values.put(TaskTable.Cols.START_TIME, task.getStartTime().getTime().getTime());
        values.put(TaskTable.Cols.END_TIME, task.getEndTime().getTime().getTime());
        values.put(TaskTable.Cols.DEFER_UNTIL, task.getDeferUntil().getTime().getTime());
        values.put(TaskTable.Cols.DEADLINE, task.getDeadline().getTime().getTime());
        values.put(TaskTable.Cols.CHECKED, String.valueOf(task.isChecked()));
        return values;
    }

    public void addTask(Task t) {
        ContentValues values = getContentValues(t);
        mDatabase.insert(TaskTable.NAME, null, values);

    }

    public List<Task> getTasksByDay(int dayOfYear) {
        List<Task> tasks = getTasks();
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task t = iterator.next();
            if (cal2day(t.getDeferUntil()) != dayOfYear) {
                iterator.remove();
            }
        }
        return tasks;
    }

    public List<Task> getTodayTasks() {
        List<Task> tasks = getTasks();
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task t = iterator.next();
            if (t.isChecked() || t.getDeferUntil().after(cal2dateCalendar(Calendar.getInstance()))) {
                iterator.remove();
            }
        }
        return tasks;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        try (TaskCursorWrapper cursor = queryTasks(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        }
        return tasks;
    }

    public List<Task> getTasksByCategory(boolean isChecked, UUID categoryId) {
        List<Task> tasks = new ArrayList<>();
        try (TaskCursorWrapper cursor = queryTasks(
                TaskTable.Cols.CHECKED + " = ? and " + TaskTable.Cols.CATEGORY + " = ?",
                new String[]{String.valueOf(isChecked), categoryId.toString()})) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        }
        return tasks;
    }

    public List<Task> queryTasks(String query) {
        List<Task> tasks = new ArrayList<>();
        try (TaskCursorWrapper cursor = queryTasks(
                TaskTable.Cols.TITLE + " like ? or " + TaskTable.Cols.NOTES + " like ?",
                new String[]{'%' + query + '%', '%' + query + '%'})) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        }
        return tasks;
    }

    public Task getTask(UUID id) {
        try (TaskCursorWrapper cursor = queryTasks(
                TaskTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getTask();
        }
    }

    public void updateTask(Task task) {
        String uuidString = task.getId().toString();
        ContentValues values = getContentValues(task);
        mDatabase.update(TaskTable.NAME, values,
                TaskTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void deleteTask(Task task) {
        String uuidString = task.getId().toString();
        mDatabase.delete(TaskTable.NAME,
                TaskTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void clearNoTitle() {
        mDatabase.delete(TaskTable.NAME,
                TaskTable.Cols.TITLE + " is ?",
                null);
    }

    private TaskCursorWrapper queryTasks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TaskTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new TaskCursorWrapper(cursor);
    }
}
