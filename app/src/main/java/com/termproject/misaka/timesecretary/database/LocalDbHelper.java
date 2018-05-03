package com.termproject.misaka.timesecretary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.termproject.misaka.timesecretary.database.CategoryDbSchema.CategoryTable;
import com.termproject.misaka.timesecretary.database.EventDbSchema.EventTable;
import com.termproject.misaka.timesecretary.database.TaskDbSchema.TaskTable;

import java.util.UUID;

public class LocalDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "timeSecretary.db";
    private Context mContext;

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EventTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                EventTable.Cols.UUID + ", " +
                EventTable.Cols.CATEGORY + ", " +
                EventTable.Cols.TITLE + ", " +
                EventTable.Cols.NOTES + ", " +
                EventTable.Cols.START_TIME + ", " +
                EventTable.Cols.END_TIME +
                ")"
        );
        db.execSQL("create table " + TaskTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                TaskTable.Cols.UUID + ", " +
                TaskTable.Cols.CATEGORY + ", " +
                TaskTable.Cols.TITLE + ", " +
                TaskTable.Cols.NOTES + ", " +
                TaskTable.Cols.START_TIME + ", " +
                TaskTable.Cols.END_TIME + ", " +
                TaskTable.Cols.DEFER_UNTIL + ", " +
                TaskTable.Cols.DEADLINE + ", " +
                TaskTable.Cols.CHECKED +
                ")"
        );
        db.execSQL("create table " + CategoryTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CategoryTable.Cols.UUID + ", " +
                CategoryTable.Cols.TITLE + ", " +
                CategoryTable.Cols.COLOR +
                ")"
        );
        db.execSQL("INSERT INTO categories (uuid, title, color) VALUES (?,?,?);", new String[]{UUID.randomUUID().toString(), "Default", "#66CCFF"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
