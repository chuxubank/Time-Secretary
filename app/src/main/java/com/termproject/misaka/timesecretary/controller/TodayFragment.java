package com.termproject.misaka.timesecretary.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.module.EventLab;
import com.termproject.misaka.timesecretary.module.Task;
import com.termproject.misaka.timesecretary.module.TaskLab;

import java.util.List;

/**
 * @author misaka
 */
public class TodayFragment extends Fragment {

    private static final String TAG = "TodayFragment";
    private RecyclerView mRvEvent;
    private RecyclerView mRvTask;
    private EventAdapter mEventAdapter;
    private TaskAdapter mTaskAdapter;
    private CategoryLab mCategoryLab;
    private MainActivity mActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);
        Log.i(TAG, "onCreateView");
        initView(v);
        updateUI();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        updateUI();
    }

    private void initView(View v) {
        mActivity = (MainActivity) getActivity();
        mRvEvent = v.findViewById(R.id.rv_event);
        mRvEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvTask = v.findViewById(R.id.rv_task);
        mRvTask.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void updateUI() {
        mCategoryLab = CategoryLab.get(getActivity());
        mCategoryLab.clearNoTitle();
        EventLab eventLab = EventLab.get(getActivity());
        eventLab.clearNoTitle();
        List<Event> events = eventLab.getEvents();
        if (mEventAdapter == null) {
            mEventAdapter = new EventAdapter(events);
        } else {
            mEventAdapter.setEvents(events);
            mEventAdapter.notifyDataSetChanged();
        }
        mRvEvent.setAdapter(mEventAdapter);
        TaskLab taskLab = TaskLab.get(getActivity());
        taskLab.clearNoTitle();
        List<Task> tasks = taskLab.getTasks();
        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(tasks);
        } else {
            mTaskAdapter.setTasks(tasks);
            mTaskAdapter.notifyDataSetChanged();
        }
        mRvTask.setAdapter(mTaskAdapter);
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        private List<Event> mEvents;

        private EventAdapter(List<Event> events) {
            mEvents = events;
        }

        @NonNull
        @Override
        public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new EventHolder(layoutInflater, parent, getActivity());
        }

        @Override
        public void onBindViewHolder(@NonNull EventHolder holder, int position) {
            Event event = mEvents.get(position);
            holder.bind(event);
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }

        private void setEvents(List<Event> events) {
            mEvents = events;
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> mTasks;

        private TaskAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent, getActivity());
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

        private void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }
    }

}
