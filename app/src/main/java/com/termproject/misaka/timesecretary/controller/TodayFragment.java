package com.termproject.misaka.timesecretary.controller;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.Category;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.module.EventLab;
import com.termproject.misaka.timesecretary.module.Task;
import com.termproject.misaka.timesecretary.module.TaskLab;
import com.termproject.misaka.timesecretary.utils.TimeUtils;

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

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Event mEvent;
        private Category mCategory;
        private View mVDivider;
        private TextView mTvEventStartTime;
        private TextView mTvEventEndTime;
        private TextView mTvEventTitle;
        private TextView mTvEventNotes;

        private EventHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_event, parent, false));
            itemView.setOnClickListener(this);
            mTvEventStartTime = itemView.findViewById(R.id.tv_event_start_time);
            mTvEventEndTime = itemView.findViewById(R.id.tv_event_end_time);
            mVDivider = itemView.findViewById(R.id.divider);
            mTvEventTitle = itemView.findViewById(R.id.tv_event_title);
            mTvEventNotes = itemView.findViewById(R.id.tv_event_notes);
        }

        public void bind(Event event) {
            mEvent = event;
            mCategory = mCategoryLab.getCategory(mEvent.getCategory());
            mTvEventStartTime.setText(TimeUtils.cal2timeString(mEvent.getStartTime()));
            mTvEventEndTime.setText(TimeUtils.cal2timeString(mEvent.getEndTime()));
            mVDivider.getBackground().setTint(Color.parseColor(mCategory.getColor()));
            mTvEventTitle.setText(mEvent.getTitle());
            mTvEventNotes.setText(mEvent.getNotes());
        }

        @Override
        public void onClick(View v) {
            Intent intent = EventActivity.newIntent(getActivity(), mEvent.getId());
            startActivity(intent);
        }
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
            return new EventHolder(layoutInflater, parent);
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

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Task mTask;
        private Category mCategory;
        private TextView mTvTaskTitle;
        private TextView mTvTaskNotes;
        private CheckBox mCbTaskChecked;

        private TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);
            mTvTaskTitle = itemView.findViewById(R.id.tv_task_title);
            mTvTaskNotes = itemView.findViewById(R.id.tv_task_notes);
            mCbTaskChecked = itemView.findViewById(R.id.cb_task_checked);
        }

        public void bind(Task task) {
            mTask = task;
            mCategory = mCategoryLab.getCategory(mTask.getCategory());
            mCbTaskChecked.setButtonTintList(new ColorStateList(
                    new int[][]{
                            new int[]{}
                    },
                    new int[]{
                            Color.parseColor(mCategory.getColor()),
                    }
            ));
            mTvTaskTitle.setText(mTask.getTitle());
            mTvTaskNotes.setText(mTask.getNotes());
        }

        @Override
        public void onClick(View v) {
            Intent intent = TaskActivity.newIntent(getActivity(), mTask.getId());
            startActivity(intent);
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
            return new TaskHolder(layoutInflater, parent);
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
