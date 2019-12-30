package com.termproject.misaka.timesecretary.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.termproject.misaka.timesecretary.controller.holder.EventHolder;
import com.termproject.misaka.timesecretary.controller.holder.TaskHolder;
import com.termproject.misaka.timesecretary.module.Entity;
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.module.Task;

import java.util.List;

public class EntityAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_EVENT = 0;
    private static final int VIEW_TYPE_TASK = 1;
    private List<Entity> mEntities;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    public EntityAdapter(List<Entity> entities, Context context, Fragment fragment, FragmentManager fragmentManager) {
        mEntities = entities;
        mContext = context;
        mFragment = fragment;
        mFragmentManager = fragmentManager;
    }

    @Override
    public int getItemViewType(int position) {
        Entity entity = mEntities.get(position);
        if (entity instanceof Event) {
            return VIEW_TYPE_EVENT;
        } else {
            return VIEW_TYPE_TASK;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case VIEW_TYPE_EVENT:
                viewHolder = new EventHolder(layoutInflater, parent, mContext);
                break;
            case VIEW_TYPE_TASK:
                viewHolder = new TaskHolder(layoutInflater, parent, mContext, mFragment, mFragmentManager);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventHolder) {
            ((EventHolder) holder).bind((Event) mEntities.get(position));
        } else {
            ((TaskHolder) holder).bind((Task) mEntities.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mEntities.size();
    }

    public void setEntities(List<Entity> entities) {
        mEntities = entities;
    }
}
