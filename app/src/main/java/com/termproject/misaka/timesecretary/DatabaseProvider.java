package com.termproject.misaka.timesecretary;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.termproject.misaka.timesecretary.database.CategoryDbSchema.CategoryTable;
import com.termproject.misaka.timesecretary.database.EventDbSchema.EventTable;
import com.termproject.misaka.timesecretary.database.LocalDbHelper;
import com.termproject.misaka.timesecretary.database.TaskDbSchema.TaskTable;

public class DatabaseProvider extends ContentProvider {

    public static final int EVENT_DIR = 0;
    public static final int EVENT_ITEM = 1;
    public static final int TASK_DIR = 2;
    public static final int TASK_ITEM = 3;
    public static final int CATEGORY_DIR = 4;
    public static final int CATEGORY_ITEM = 5;
    public static final String AUTHORITY = "com.termproject.misaka.timesecretary.provider";
    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, EventTable.NAME, EVENT_DIR);
        uriMatcher.addURI(AUTHORITY, EventTable.NAME + "/#", EVENT_ITEM);
        uriMatcher.addURI(AUTHORITY, TaskTable.NAME, TASK_DIR);
        uriMatcher.addURI(AUTHORITY, TaskTable.NAME + "/#", TASK_ITEM);
        uriMatcher.addURI(AUTHORITY, CategoryTable.NAME, CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, CategoryTable.NAME + "/#", CATEGORY_ITEM);
    }

    private LocalDbHelper mDBHelper;

    public DatabaseProvider() {
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new LocalDbHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)) {
            case EVENT_DIR:
                deleteRows = db.delete(EventTable.NAME, selection, selectionArgs);
                break;
            case EVENT_ITEM:
                String eventId = uri.getPathSegments().get(1);
                deleteRows = db.delete(EventTable.NAME, EventTable.Cols.UUID + " = ?", new String[]{eventId});
                break;
            case TASK_DIR:
                deleteRows = db.delete(TaskTable.NAME, selection, selectionArgs);
                break;
            case TASK_ITEM:
                String taskId = uri.getPathSegments().get(1);
                deleteRows = db.delete(TaskTable.NAME, TaskTable.Cols.UUID + " = ?", new String[]{taskId});
                break;
            case CATEGORY_DIR:
                deleteRows = db.delete(CategoryTable.NAME, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRows = db.delete(CategoryTable.NAME, CategoryTable.Cols.UUID + " = ?", new String[]{categoryId});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case EVENT_DIR:
                return String.format("vnd.android.cursor.dir/vnd.%s.%s", AUTHORITY, EventTable.NAME);
            case EVENT_ITEM:
                return String.format("vnd.android.cursor.item/vnd.%s.%s", AUTHORITY, EventTable.NAME);
            case TASK_DIR:
                return String.format("vnd.android.cursor.dir/vnd.%s.%s", AUTHORITY, TaskTable.NAME);
            case TASK_ITEM:
                return String.format("vnd.android.cursor.item/vnd.%s.%s", AUTHORITY, TaskTable.NAME);
            case CATEGORY_DIR:
                return String.format("vnd.android.cursor.dir/vnd.%s.%s", AUTHORITY, CategoryTable.NAME);
            case CATEGORY_ITEM:
                return String.format("vnd.android.cursor.item/vnd.%s.%s", AUTHORITY, CategoryTable.NAME);
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case EVENT_DIR:
            case EVENT_ITEM:
                db.insert(EventTable.NAME, null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/" + EventTable.NAME + "/" + values.getAsString(EventTable.Cols.UUID));
                break;
            case TASK_DIR:
            case TASK_ITEM:
                db.insert(TaskTable.NAME, null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/" + TaskTable.NAME + "/" + values.getAsString(TaskTable.Cols.UUID));
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                db.insert(CategoryTable.NAME, null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/" + CategoryTable.NAME + "/" + values.getAsString(CategoryTable.Cols.UUID));
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case EVENT_DIR:
                cursor = db.query(EventTable.NAME, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EVENT_ITEM:
                String eventId = uri.getPathSegments().get(1);
                cursor = db.query(EventTable.NAME, projection,
                        EventTable.Cols.UUID + " = ?", new String[]{eventId},
                        null, null, sortOrder);
                break;
            case TASK_DIR:
                cursor = db.query(TaskTable.NAME, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TASK_ITEM:
                String taskId = uri.getPathSegments().get(1);
                cursor = db.query(TaskTable.NAME, projection,
                        TaskTable.Cols.UUID + " = ?", new String[]{taskId},
                        null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query(CategoryTable.NAME, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query(CategoryTable.NAME, projection,
                        CategoryTable.Cols.UUID + " = ?", new String[]{categoryId},
                        null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)) {
            case EVENT_DIR:
                updateRows = db.update(EventTable.NAME, values,
                        selection, selectionArgs);
                break;
            case EVENT_ITEM:
                String eventId = uri.getPathSegments().get(1);
                updateRows = db.update(EventTable.NAME, values,
                        EventTable.Cols.UUID + " = ?", new String[]{eventId});
                break;
            case TASK_DIR:
                updateRows = db.update(TaskTable.NAME, values,
                        selection, selectionArgs);
                break;
            case TASK_ITEM:
                String taskId = uri.getPathSegments().get(1);
                updateRows = db.update(TaskTable.NAME, values,
                        TaskTable.Cols.UUID + " = ?", new String[]{taskId});
                break;
            case CATEGORY_DIR:
                updateRows = db.update(CategoryTable.NAME, values,
                        selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = db.update(CategoryTable.NAME, values,
                        CategoryTable.Cols.UUID + " = ?", new String[]{categoryId});
                break;
            default:
                break;
        }
        return updateRows;
    }
}
