package com.termproject.misaka.timesecretary.controller.holder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.controller.activity.TaskActivity;
import com.termproject.misaka.timesecretary.module.Category;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Task;
import com.termproject.misaka.timesecretary.module.TaskLab;
import com.termproject.misaka.timesecretary.part.TaskTimeFragment;

import java.util.Calendar;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateCalendar;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateTimeCalendar;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2long;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.diffOfDay;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.long2calendar;

public class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "TaskHolder";
    private static final String DIALOG_TASK_TIME = "DialogTaskTime";
    private static final int REQUEST_TAKE_TIME = -1;
    private Task mTask;
    private TextView mTvTaskTitle;
    private TextView mTvTaskNotes;
    private TextView mTvDeadline;
    private CheckBox mCbTaskChecked;
    private CategoryLab mCategoryLab;
    private Context mContext;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    public TaskHolder(LayoutInflater inflater, ViewGroup parent, Context context, Fragment fragment, FragmentManager fragmentManager) {
        super(inflater.inflate(R.layout.list_item_task, parent, false));
        itemView.setOnClickListener(this);
        mContext = context;
        mFragment = fragment;
        mFragmentManager = fragmentManager;
        mCategoryLab = CategoryLab.get(mContext);
        mTvTaskTitle = itemView.findViewById(R.id.tv_task_title);
        mTvTaskNotes = itemView.findViewById(R.id.tv_task_notes);
        mTvDeadline = itemView.findViewById(R.id.tv_deadline);
        mCbTaskChecked = itemView.findViewById(R.id.cb_task_checked);
    }

    public void bind(Task task) {
        mTask = task;
        Category category = mCategoryLab.getCategory(mTask.getCategory());
        mCbTaskChecked.setChecked(mTask.isChecked());
        mCbTaskChecked.setButtonTintList(new ColorStateList(
                new int[][]{new int[]{}},
                new int[]{Color.parseColor(category.getColor()),}
        ));
        mCbTaskChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && !mTask.isChecked()) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    int taskTime = Integer.parseInt(preferences.getString("default_task_time", "15"));
                    if (taskTime != 0) {
                        Calendar startTime = cal2dateTimeCalendar(Calendar.getInstance());
                        Calendar endTime = cal2dateTimeCalendar(Calendar.getInstance());
                        startTime.add(Calendar.MINUTE, -taskTime);
                        if (cal2long(mTask.getStartTime()) == 0 && cal2long(mTask.getEndTime()) == 0) {
                            mTask.setStartTime(startTime);
                            mTask.setEndTime(endTime);
                            TaskLab.get(mContext).updateTask(mTask);
                        }
                        TaskTimeFragment taskTimeFragment = TaskTimeFragment.newInstance(mTask.getId());
                        taskTimeFragment.setTargetFragment(mFragment, REQUEST_TAKE_TIME);
                        taskTimeFragment.show(mFragmentManager, DIALOG_TASK_TIME);
                    } else {
                        mTask.setChecked(true);
                        TaskLab.get(mContext).updateTask(mTask);
                    }
                } else if (!isChecked && mTask.isChecked()) {
                    mTask.setChecked(false);
                    mTask.setStartTime(long2calendar(0));
                    mTask.setEndTime(long2calendar(0));
                    TaskLab.get(mContext).updateTask(mTask);
                    mFragment.onResume();
                }
            }
        });
        mTvTaskTitle.setText(mTask.getTitle());
        if (mTask.getNotes().isEmpty()) {
            mTvTaskNotes.setVisibility(View.GONE);
        } else {
            mTvTaskNotes.setText(mTask.getNotes());
        }
        if (!mTask.isChecked()) {
            mTvDeadline.setText(diffOfDay(mTask.getDeadline()));
        }
        if (!mTask.getDeadline().after(cal2dateCalendar(Calendar.getInstance()))) {
            mTvDeadline.setTextColor(ContextCompat.getColor(mContext, R.color.accent));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = TaskActivity.newIntent(mContext, mTask.getId());
        mContext.startActivity(intent);
    }
}
