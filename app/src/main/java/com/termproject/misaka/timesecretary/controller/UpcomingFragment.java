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
import com.termproject.misaka.timesecretary.part.DateDividerItemDecoration;
import com.termproject.misaka.timesecretary.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateString;

public class UpcomingFragment extends Fragment {

    private static final String TAG = "UpcomingFragment";
    private EventTaskAdapter mAdapter;
    private RecyclerView mRvUpcoming;
    private CategoryLab mCategoryLab;
    private List<Object> mObjects;
    private MainActivity mActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upcoming, container, false);
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
        mRvUpcoming = v.findViewById(R.id.rv_upcoming);
        mRvUpcoming.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvUpcoming.addItemDecoration(new DateDividerItemDecoration(getActivity(), new DateDividerItemDecoration.ItemDecorationCallback() {
            @Override
            public String getGroupName(int position) {
                Object o = mObjects.get(position);
                if (o instanceof Event) {
                    return cal2dateString(((Event) o).getStartTime());
                } else if (o instanceof Task) {
                    return cal2dateString(((Task) o).getDeferUntil());
                } else {
                    return null;
                }
            }
        }));
    }

    private void updateUI() {
        mCategoryLab = CategoryLab.get(getActivity());
        mCategoryLab.clearNoTitle();
        EventLab eventLab = EventLab.get(getActivity());
        eventLab.clearNoTitle();
        List<Event> events = eventLab.getEvents();
        TaskLab taskLab = TaskLab.get(getActivity());
        taskLab.clearNoTitle();
        List<Task> tasks = taskLab.getTasks();
        Collections.sort(events);
        Collections.sort(tasks);
        Set<Integer> days = new TreeSet<>();
        for (Event e : events) {
            days.add(e.getStartTime().get(Calendar.DAY_OF_YEAR));
        }
        for (Task t : tasks) {
            days.add(t.getDeferUntil().get(Calendar.DAY_OF_YEAR));
        }
        mObjects = new ArrayList<>();
        for (Integer day : days) {
            List<Event> dayEvents = eventLab.getEventsByDayOfYear(day);
            Collections.sort(dayEvents);
            List<Task> dayTasks = taskLab.getTasksByDayOfYear(day);
            Collections.sort(dayTasks);
            mObjects.addAll(dayEvents);
            mObjects.addAll(dayTasks);
        }
        if (mAdapter == null) {
            mAdapter = new EventTaskAdapter(mObjects);
        } else {
            mAdapter.setObjects(mObjects);
            mAdapter.notifyDataSetChanged();
        }
        mRvUpcoming.setAdapter(mAdapter);
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

    private class EventTaskAdapter extends RecyclerView.Adapter {

        private static final int VIEW_TYPE_EVENT = 0;
        private static final int VIEW_TYPE_TASK = 1;
        private List<Object> mObjects;

        private EventTaskAdapter(List<Object> objects) {
            mObjects = objects;
        }

        @Override
        public int getItemViewType(int position) {
            Object object = mObjects.get(position);
            if (object instanceof Event) {
                return VIEW_TYPE_EVENT;
            } else {
                return VIEW_TYPE_TASK;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            switch (viewType) {
                case VIEW_TYPE_EVENT:
                    viewHolder = new UpcomingFragment.EventHolder(layoutInflater, parent);
                    break;
                case VIEW_TYPE_TASK:
                    viewHolder = new UpcomingFragment.TaskHolder(layoutInflater, parent);
                    break;
                default:
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof EventHolder) {
                ((UpcomingFragment.EventHolder) holder).bind((Event) mObjects.get(position));
            } else {
                ((UpcomingFragment.TaskHolder) holder).bind((Task) mObjects.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mObjects.size();
        }

        private void setObjects(List<Object> objects) {
            mObjects = objects;
        }
    }
}
