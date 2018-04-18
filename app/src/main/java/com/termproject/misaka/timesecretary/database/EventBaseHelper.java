package com.termproject.misaka.timesecretary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.termproject.misaka.timesecretary.database.EventDbSchema.EventTable;

public class EventBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "eventBase.db";

    public EventBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EventTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                EventTable.Cols.UUID + ", " +
                EventTable.Cols.TITLE + ", " +
                EventTable.Cols.DESCRIPTION + ", " +
                EventTable.Cols.AS_TASK + ", " +
                EventTable.Cols.COMPLETED + ", " +
                EventTable.Cols.START_DATE + ", " +
                EventTable.Cols.END_DATE +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
