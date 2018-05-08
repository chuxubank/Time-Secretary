package com.termproject.misaka.timesecretary.controller;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

public class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "TaskHolder";
    private Task mTask;
    private Category mCategory;
    private TextView mTvTaskTitle;
    private TextView mTvTaskNotes;
    private CheckBox mCbTaskChecked;
    private CategoryLab mCategoryLab;
    private Context mContext;

    TaskHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.list_item_task, parent, false));
        itemView.setOnClickListener(this);
        mContext = context;
        mCategoryLab = CategoryLab.get(mContext);
        mTvTaskTitle = itemView.findViewById(R.id.tv_task_title);
        mTvTaskNotes = itemView.findViewById(R.id.tv_task_notes);
        mCbTaskChecked = itemView.findViewById(R.id.cb_task_checked);
    }

    public void bind(Task task) {
        mTask = task;
        mCategory = mCategoryLab.getCategory(mTask.getCategory());
        mCbTaskChecked.setChecked(mTask.isChecked());
        mCbTaskChecked.setButtonTintList(new ColorStateList(
                new int[][]{
                        new int[]{}
                },
                new int[]{
                        Color.parseColor(mCategory.getColor()),
                }
        ));
        mCbTaskChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTask.setChecked(isChecked);
                if (isChecked) {

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
