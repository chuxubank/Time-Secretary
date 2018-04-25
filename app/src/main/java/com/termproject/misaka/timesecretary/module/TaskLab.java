package com.termproject.misaka.timesecretary.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author misaka
 */
public class TaskLab {
    private static TaskLab sTaskLab;
    private List<Task> mTasks;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private TaskLab(Context context) {
        mContext = context.getApplicationContext();
        mTasks = new ArrayList<>();
    }

    public void addTask(Task t) {
        mTasks.add(t);
    }

    public void deleteTask(Task t) {
        mTasks.remove(t);
    }

    public void clearNoTitle() {
        for (Task t : mTasks) {
            if (TextUtils.isEmpty(t.getTitle())) {
                mTasks.remove(t);
            }
        }
    }
    
    public static TaskLab get(Context context) {
        if (sTaskLab == null) {
            sTaskLab = new TaskLab(context);
        }
        return sTaskLab;
    }

    public List<Task> getTasks() {
        return mTasks;
    }

    public Task getTask(UUID id) {
        for (Task task : mTasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }
}
