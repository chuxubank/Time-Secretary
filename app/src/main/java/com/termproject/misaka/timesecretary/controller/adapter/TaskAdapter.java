package com.termproject.misaka.timesecretary.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.termproject.misaka.timesecretary.controller.holder.TaskHolder;
import com.termproject.misaka.timesecretary.module.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
    private List<Task> mTasks;
    private Context mContext;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    public TaskAdapter(List<Task> tasks, Context context, Fragment fragment, FragmentManager fragmentManager) {
        mTasks = tasks;
        mContext = context;
        mFragment = fragment;
        mFragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new TaskHolder(layoutInflater, parent, mContext, mFragment, mFragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public void setTasks(List<Task> tasks) {
        mTasks = tasks;
    }
}

