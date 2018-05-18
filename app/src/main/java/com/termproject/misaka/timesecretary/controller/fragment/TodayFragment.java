package com.termproject.misaka.timesecretary.controller.fragment;

import android.content.Intent;
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
        mRvEvent = v.findViewById(R.id.rv_event);
        mRvEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvTask = v.findViewById(R.id.rv_task);
        mRvTask.setLayoutManager(new LinearLayoutManager(getActivity()));
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUI();
    }
}
