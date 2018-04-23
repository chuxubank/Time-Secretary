package com.termproject.misaka.timesecretary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

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

    private RecyclerView mEventRecyclerView;
    private RecyclerView mTaskRecyclerView;
    private EventAdapter mEventAdapter;
    private TaskAdapter mTaskAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);
        initView(v);
        updateUI();
        return v;
    }

    private void initView(View v) {
        mEventRecyclerView = v.findViewById(R.id.event_recycler_view);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskRecyclerView = v.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void updateUI() {
        EventLab eventLab = EventLab.get(getActivity());
        List<Event> events = eventLab.getEvents();
        mEventAdapter = new EventAdapter(events);
        mEventRecyclerView.setAdapter(mEventAdapter);
        TaskLab taskLab = TaskLab.get(getActivity());
        List<Task> tasks = taskLab.getTasks();
        mTaskAdapter = new TaskAdapter(tasks);
        mTaskRecyclerView.setAdapter(mTaskAdapter);
    }

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Event mEvent;
        private TextView mEventTitle;
        private TextView mEventNotes;
        private TextView mEventStartTime;
        private TextView mEventEndTime;

        public EventHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_event, parent, false));
            itemView.setOnClickListener(this);
            mEventTitle = itemView.findViewById(R.id.event_title);
            mEventNotes = itemView.findViewById(R.id.event_notes);
            mEventStartTime = itemView.findViewById(R.id.event_start_time);
            mEventEndTime = itemView.findViewById(R.id.event_end_time);
        }

        public void bind(Event event) {
            mEvent = event;
            mEventTitle.setText(mEvent.getTitle());
            mEventNotes.setText(mEvent.getNotes());
            mEventStartTime.setText(TimeUtils.cal2timeString(mEvent.getStartTime()));
            mEventEndTime.setText(TimeUtils.cal2timeString(mEvent.getEndTime()));
        }

        @Override
        public void onClick(View v) {
            Snackbar.make(getView(), mEvent.getTitle(), Snackbar.LENGTH_SHORT).show();
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Task mTask;
        private TextView mTaskTitle;
        private TextView mTaskNotes;
        private CheckBox mTaskChecked;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);
            mTaskTitle = itemView.findViewById(R.id.task_title);
            mTaskNotes = itemView.findViewById(R.id.task_notes);
            mTaskChecked = itemView.findViewById(R.id.task_checked);
        }

        public void bind(Task task) {
            mTask = task;
            mTaskTitle.setText(mTask.getTitle());
            mTaskNotes.setText(mTask.getNotes());
        }

        @Override
        public void onClick(View v) {
            Snackbar.make(getView(), mTask.getTitle(), Snackbar.LENGTH_SHORT).show();
        }
    }


    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        private List<Event> mEvents;

        public EventAdapter(List<Event> events) {
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
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> mTasks;

        public TaskAdapter(List<Task> tasks) {
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
    }

}
