package com.termproject.misaka.timesecretary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
    private int mSize = 3;

    private TaskLab(Context context) {
        mContext = context.getApplicationContext();

        mTasks = new ArrayList<>();
        for (int i = 0; i < mSize; i++) {
            Task task = new Task();
            task.setTitle("Task #" + i);
            mTasks.add(task);
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
