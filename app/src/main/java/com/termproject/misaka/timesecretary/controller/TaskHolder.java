package com.termproject.misaka.timesecretary.controller;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.Category;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Task;
import com.termproject.misaka.timesecretary.module.TaskLab;
import com.termproject.misaka.timesecretary.part.TaskTimeFragment;

import java.util.Calendar;
import java.util.List;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2long;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.long2calendar;

public class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "TaskHolder";
    private static final String DIALOG_TASK_TIME = "DialogTaskTime";
    private static final int REQUEST_TAKE_TIME = -1;
    private Task mTask;
    private TextView mTvTaskTitle;
    private TextView mTvTaskNotes;
    private CheckBox mCbTaskChecked;
    private CategoryLab mCategoryLab;
    private Context mContext;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    TaskHolder(LayoutInflater inflater, ViewGroup parent, Context context, Fragment fragment, FragmentManager fragmentManager) {
        super(inflater.inflate(R.layout.list_item_task, parent, false));
        itemView.setOnClickListener(this);
        mContext = context;
        mFragment = fragment;
        mFragmentManager = fragmentManager;
        mCategoryLab = CategoryLab.get(mContext);
        mTvTaskTitle = itemView.findViewById(R.id.tv_task_title);
        mTvTaskNotes = itemView.findViewById(R.id.tv_task_notes);
        mCbTaskChecked = itemView.findViewById(R.id.cb_task_checked);
    }

    public void bind(Task task) {
        mTask = task;
        Category category = mCategoryLab.getCategory(mTask.getCategory());
        mCbTaskChecked.setChecked(mTask.isChecked());
        mCbTaskChecked.setButtonTintList(new ColorStateList(
                new int[][]{
                        new int[]{}
                },
                new int[]{
                        Color.parseColor(category.getColor()),
                }
        ));
        mCbTaskChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mTask.setChecked(isChecked);
                if (isChecked) {
                    Calendar startTime = Calendar.getInstance();
                    Calendar endTime = Calendar.getInstance();
                    endTime.add(Calendar.MINUTE, 30);
                    if (cal2long(mTask.getStartTime()) == 0 && cal2long(mTask.getEndTime()) == 0) {
                        mTask.setStartTime(startTime);
                        mTask.setEndTime(endTime);
                        TaskLab.get(mContext).updateTask(mTask);
                    }
                    TaskTimeFragment taskTime = TaskTimeFragment.newInstance(mTask.getId());
                    taskTime.setTargetFragment(mFragment, REQUEST_TAKE_TIME);
                    taskTime.show(mFragmentManager, DIALOG_TASK_TIME);
                } else {
                    mTask.setStartTime(long2calendar(0));
                    mTask.setEndTime(long2calendar(0));
                    TaskLab.get(mContext).updateTask(mTask);
                    List<Fragment> fragments = mFragmentManager.getFragments();
                    for (Fragment f : fragments) {
                        f.onResume();
                    }
                }
            }
        });
        mTvTaskTitle.setText(mTask.getTitle());
        mTvTaskNotes.setText(mTask.getNotes());
    }

    @Override
    public void onClick(View v) {
        Intent intent = TaskActivity.newIntent(mContext, mTask.getId());
        mContext.startActivity(intent);
    }
}
