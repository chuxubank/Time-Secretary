package com.termproject.misaka.timesecretary.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.termproject.misaka.timesecretary.base.BaseSingleFragmentActivity;

import java.util.UUID;

public class TaskActivity extends BaseSingleFragmentActivity {

    private static final String EXTRA_TASK_ID = "com.termproject.misaka.timesecretary.task_id";

    public static Intent newIntent(Context packageContext, UUID taskId) {
        Intent intent = new Intent(packageContext, TaskActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        UUID taskId = (UUID) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        return TaskFragment.newInstance(taskId);
    }
}
