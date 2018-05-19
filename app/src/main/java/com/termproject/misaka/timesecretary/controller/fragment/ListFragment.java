package com.termproject.misaka.timesecretary.controller.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.controller.adapter.EntityAdapter;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Entity;
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.module.EventLab;
import com.termproject.misaka.timesecretary.module.Task;
import com.termproject.misaka.timesecretary.module.TaskLab;
import com.termproject.misaka.timesecretary.part.DateDividerItemDecoration;
import com.termproject.misaka.timesecretary.part.DatePickerFragment;
import com.termproject.misaka.timesecretary.part.EntityDividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2day;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2longDateString;

public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_TAKE_TIME = -1;
    private static final int REQUEST_SELECTED_DATE = 0;
    private EntityAdapter mAdapter;
    private RecyclerView mRvUpcoming;
    private List<Entity> mEntities;
    private String queryText;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_menu, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryText = newText;
                queryEntities(queryText);
                return true;
            }
        });
    }

    private void queryEntities(String query) {
        mEntities = getSortedEntities(
                EventLab.get(getActivity()).queryEvents(query),
                TaskLab.get(getActivity()).queryTasks(query));
        mAdapter.setEntities(mEntities);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select_date:
                DatePickerFragment startDate = DatePickerFragment.newInstance(Calendar.getInstance());
                startDate.setTargetFragment(ListFragment.this, REQUEST_SELECTED_DATE);
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
            int selectedDay = cal2day(calendar);
            List<Integer> days = new ArrayList<>();
            for (Entity entity : mEntities) {
                Calendar cal;
                if (entity instanceof Event) {
                    cal = entity.getStartTime();
                } else {
                    cal = ((Task) entity).getDeferUntil();
                }
                days.add(cal2day(cal));
            }
            Collections.sort(days);
            int pos = bound(days, selectedDay, true);
            Log.d(TAG, "pos:" + pos);
            if (pos < 0) {
                Snackbar.make(getView(), getString(R.string.error_no_event_task), Snackbar.LENGTH_SHORT).show();
            } else {
                ((LinearLayoutManager) mRvUpcoming.getLayoutManager()).scrollToPositionWithOffset(pos, 0);
            }
        } else {
            updateUI();
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
        View v = inflater.inflate(R.layout.fragment_list, container, false);
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
        setHasOptionsMenu(true);
        mRvUpcoming = v.findViewById(R.id.rv_list);
        mRvUpcoming.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvUpcoming.addItemDecoration(new EntityDividerItemDecoration(getActivity(), new EntityDividerItemDecoration.EntityDividerCallback() {
            @Override
            public String getEntityName(int position) {
                Entity entity = mEntities.get(position);
                if (entity instanceof Event) {
                    return "Event";
                } else if (entity instanceof Task) {
                    return "Task";
                } else {
                    return null;
                }
            }

            @Override
            public String getDateString(int position) {
                Entity entity = mEntities.get(position);
                if (entity instanceof Event) {
                    return cal2longDateString(entity.getStartTime());
                } else if (entity instanceof Task) {
                    return cal2longDateString(((Task) entity).getDeferUntil());
                } else {
                    return null;
                }
            }
        }));
        mRvUpcoming.addItemDecoration(new DateDividerItemDecoration(getActivity(), new DateDividerItemDecoration.DateDividerCallback() {
            @Override
            public String getDateString(int position) {
                Entity entity = mEntities.get(position);
                if (entity instanceof Event) {
                    return cal2longDateString(entity.getStartTime());
                } else if (entity instanceof Task) {
                    return cal2longDateString(((Task) entity).getDeferUntil());
                } else {
                    return null;
                }
            }
        }));
    }

    private void updateUI() {
        CategoryLab categoryLab = CategoryLab.get(getActivity());
        categoryLab.clearNoTitle();
        EventLab eventLab = EventLab.get(getActivity());
        eventLab.clearNoTitle();
        TaskLab taskLab = TaskLab.get(getActivity());
        taskLab.clearNoTitle();
        if (queryText == null || queryText.isEmpty()) {
            mEntities = getSortedEntities(eventLab.getEvents(), taskLab.getTasks());
        } else {
            mEntities = getSortedEntities(
                    EventLab.get(getActivity()).queryEvents(queryText),
                    TaskLab.get(getActivity()).queryTasks(queryText));
        }
        if (mAdapter == null) {
            mAdapter = new EntityAdapter(mEntities, getActivity(), this, getFragmentManager());
        } else {
            mAdapter.setEntities(mEntities);
            mAdapter.notifyDataSetChanged();
        }
        mRvUpcoming.setAdapter(mAdapter);
    }

    private List<Entity> getSortedEntities(List<Event> events, List<Task> tasks) {
        List<Entity> entities = new ArrayList<>();
        Set<Integer> days = new TreeSet<>();
        for (Event e : events) {
            days.add(cal2day(e.getStartTime()));
        }
        for (Task t : tasks) {
            days.add(cal2day(t.getDeferUntil()));
        }
        Collections.sort(events);
        Collections.sort(tasks);
        for (Integer day : days) {
            for (Event e : events) {
                if (cal2day(e.getStartTime()) == day) {
                    entities.add(e);
                }
            }
            for (Task t : tasks) {
                if (cal2day(t.getDeferUntil()) == day) {
                    entities.add(t);
                }
            }
        }
        return entities;
    }
}
