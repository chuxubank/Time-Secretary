package com.termproject.misaka.timesecretary.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.module.EventLab;
import com.termproject.misaka.timesecretary.module.Task;
import com.termproject.misaka.timesecretary.module.TaskLab;
import com.termproject.misaka.timesecretary.part.DateDividerItemDecoration;
import com.termproject.misaka.timesecretary.part.DatePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2day;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2longDateString;

public class UpcomingFragment extends Fragment {

    private static final String TAG = "UpcomingFragment";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_TAKE_TIME = -1;
    private static final int REQUEST_SELECTED_DATE = 0;
    private EventTaskAdapter mAdapter;
    private RecyclerView mRvUpcoming;
    private CategoryLab mCategoryLab;
    private List<Object> mObjects;
    private int mSelectedDay;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_upcoming_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select_date:
                DatePickerFragment startDate = DatePickerFragment.newInstance(Calendar.getInstance());
                startDate.setTargetFragment(UpcomingFragment.this, REQUEST_SELECTED_DATE);
                startDate.show(getFragmentManager(), DIALOG_DATE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SELECTED_DATE && resultCode == Activity.RESULT_OK) {
            Calendar calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mSelectedDay = cal2day(calendar);
            List<Integer> days = new ArrayList<>();
            for (Object o : mObjects) {
                Calendar cal;
                if (o instanceof Event) {
                    cal = ((Event) o).getStartTime();
                } else {
                    cal = ((Task) o).getDeferUntil();
                }
                days.add(cal2day(cal));
            }
            Collections.sort(days);
            int pos = bound(days, mSelectedDay, true);
            Log.d(TAG, "pos:" + pos);
            if (pos < 0) {
                Snackbar.make(getView(), getString(R.string.error_no_event_task), Snackbar.LENGTH_SHORT).show();
            } else {
                ((LinearLayoutManager) mRvUpcoming.getLayoutManager()).scrollToPositionWithOffset(pos, 0);
            }
        } else {
            updateAllCachedFragment();
        }
    }

    private void updateAllCachedFragment() {
        List<Fragment> fragments = getFragmentManager().getFragments();
        for (Fragment f : fragments) {
            f.onResume();
        }
    }

    private Integer bound(final List<Integer> integers, int key, boolean searchFirst) {
        int n = integers.size();
        int low = 0;
        int high = n - 1;
        int res = -1;
        int mid;
        while (low <= high) {
            mid = low + (high - low) / 2;
            if (integers.get(mid) == key) {
                res = mid;
                if (searchFirst) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else if (key > integers.get(mid)) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return res;
    }

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
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        updateUI();
    }

    private void initView(View v) {
        setHasOptionsMenu(true);
        mRvUpcoming = v.findViewById(R.id.rv_upcoming);
        mRvUpcoming.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvUpcoming.addItemDecoration(new DateDividerItemDecoration(getActivity(), new DateDividerItemDecoration.ItemDecorationCallback() {
            @Override
            public String getGroupName(int position) {
                Object o = mObjects.get(position);
                if (o instanceof Event) {
                    return cal2longDateString(((Event) o).getStartTime());
                } else if (o instanceof Task) {
                    return cal2longDateString(((Task) o).getDeferUntil());
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
            days.add(cal2day(e.getStartTime()));
        }
        for (Task t : tasks) {
            days.add(cal2day(t.getDeferUntil()));
        }
        mObjects = new ArrayList<>();
        for (Integer day : days) {
            List<Event> dayEvents = eventLab.getEventsByDay(day);
            List<Task> dayTasks = taskLab.getTasksByDay(day);
            Collections.sort(dayEvents);
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
                    viewHolder = new EventHolder(layoutInflater, parent, getActivity());
                    break;
                case VIEW_TYPE_TASK:
                    viewHolder = new TaskHolder(layoutInflater, parent, getActivity(), UpcomingFragment.this, getFragmentManager());
                    break;
                default:
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof EventHolder) {
                ((EventHolder) holder).bind((Event) mObjects.get(position));
            } else {
                ((TaskHolder) holder).bind((Task) mObjects.get(position));
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
