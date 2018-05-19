package com.termproject.misaka.timesecretary.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.controller.adapter.EventAdapter;
import com.termproject.misaka.timesecretary.controller.adapter.TaskAdapter;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.module.EventLab;
import com.termproject.misaka.timesecretary.module.Task;
import com.termproject.misaka.timesecretary.module.TaskLab;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2day;

/**
 * @author misaka
 */
public class TodayFragment extends Fragment {

    private static final String TAG = "TodayFragment";
    private static final int REQUEST_TAKE_TIME = -1;
    private RecyclerView mRvEvent;
    private RecyclerView mRvTask;
    private EventAdapter mEventAdapter;
    private TaskAdapter mTaskAdapter;
    private TextView mTvEvents;
    private TextView mTvTasks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);
        initView(v);
        updateUI();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void initView(View v) {
        mRvEvent = v.findViewById(R.id.rv_event);
        mRvEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvTask = v.findViewById(R.id.rv_task);
        mRvTask.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTvEvents = v.findViewById(R.id.tv_events);
        mTvTasks = v.findViewById(R.id.tv_tasks);
    }

    private void updateUI() {
        CategoryLab categoryLab = CategoryLab.get(getActivity());
        categoryLab.clearNoTitle();
        EventLab eventLab = EventLab.get(getActivity());
        eventLab.clearNoTitle();
        List<Event> events = eventLab.getEventsByDay(cal2day(Calendar.getInstance()));
        Collections.sort(events);
        if (mEventAdapter == null) {
            mEventAdapter = new EventAdapter(events, getActivity());
        } else {
            mEventAdapter.setEvents(events);
            mEventAdapter.notifyDataSetChanged();
        }
        mRvEvent.setAdapter(mEventAdapter);
        mTvEvents.setText(String.format(getString(R.string.prompt_events), events.size()));
        TaskLab taskLab = TaskLab.get(getActivity());
        taskLab.clearNoTitle();
        List<Task> tasks = taskLab.getTodayTasks();
        Collections.sort(tasks);
        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(tasks, getActivity(), this, getFragmentManager());
        } else {
            mTaskAdapter.setTasks(tasks);
            mTaskAdapter.notifyDataSetChanged();
        }
        mRvTask.setAdapter(mTaskAdapter);
        mTvTasks.setText(String.format(getString(R.string.prompt_tasks), tasks.size()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUI();
    }
}
