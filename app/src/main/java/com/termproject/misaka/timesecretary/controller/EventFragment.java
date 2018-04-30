package com.termproject.misaka.timesecretary.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.Category;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.module.EventLab;
import com.termproject.misaka.timesecretary.part.DatePickerFragment;
import com.termproject.misaka.timesecretary.part.TimePickerFragment;
import com.termproject.misaka.timesecretary.utils.TimeUtils;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * @author misaka
 */
public class EventFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_EVENT_ID = "event_id";

    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_START_DATETIME = 0;
    private static final int REQUEST_END_DATETIME = 1;
    private Event mEvent;
    private EditText mEtStartDate;
    private EditText mEtStartTime;
    private EditText mEtEndDate;
    private EditText mEtEndTime;
    private FloatingActionButton mFabConfirm;
    private Spinner mSpnCategory;
    private CategoryAdapter mCategoryAdapter;
    private Toolbar mToolbar;
    private TextInputLayout mEtTitle;
    private TextInputLayout mEtNotes;

    public static EventFragment newInstance(UUID eventId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_ID, eventId);
        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID eventId = (UUID) getArguments().getSerializable(ARG_EVENT_ID);
        mEvent = EventLab.get(getActivity()).getEvent(eventId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, container, false);
        initView(v);
        updateUI();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_START_DATETIME) {
            Calendar calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATETIME);
            mEvent.setStartTime(calendar);
            updateUI();
        } else if (requestCode == REQUEST_END_DATETIME) {
            Calendar calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATETIME);
            mEvent.setEndTime(calendar);
            updateUI();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.common_single_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (!TextUtils.isEmpty(mEtTitle.getEditText().getText().toString())) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(android.R.string.dialog_alert_title)
                            .setMessage(R.string.alert_delete)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EventLab.get(getActivity()).deleteEvent(mEvent);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .create().show();
                } else {
                    getActivity().finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView(View v) {
        mToolbar = v.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mEtTitle = v.findViewById(R.id.et_title);
        EditText mEtTitleEditText = mEtTitle.getEditText();
        mEtTitleEditText.setText(mEvent.getTitle());
        mEtTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mEtTitle.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEtNotes = v.findViewById(R.id.et_notes);
        EditText mEtNotesEditText = mEtNotes.getEditText();
        mEtNotesEditText.setText(mEvent.getNotes());
        mSpnCategory = v.findViewById(R.id.spn_category);
        CategoryLab categoryLab = CategoryLab.get(getActivity());
        mCategoryAdapter = new CategoryAdapter(categoryLab.getCategories(), getActivity());
        mSpnCategory.setAdapter(mCategoryAdapter);
        mSpnCategory.setSelection(categoryLab.getPosition(mEvent.getCategory()), true);
        mEtStartDate = v.findViewById(R.id.et_start_date);
        mEtStartDate.setOnClickListener(this);
        mEtStartTime = v.findViewById(R.id.et_start_time);
        mEtStartTime.setOnClickListener(this);
        mEtEndDate = v.findViewById(R.id.et_end_date);
        mEtEndDate.setOnClickListener(this);
        mEtEndTime = v.findViewById(R.id.et_end_time);
        mEtEndTime.setOnClickListener(this);
        mFabConfirm = v.findViewById(R.id.fab_confirm);
        mFabConfirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.et_start_date:
                DatePickerFragment startDate = DatePickerFragment.newInstance(mEvent.getStartTime());
                startDate.setTargetFragment(EventFragment.this, REQUEST_START_DATETIME);
                startDate.show(getFragmentManager(), DIALOG_DATE);
                break;
            case R.id.et_start_time:
                TimePickerFragment startTime = TimePickerFragment.newInstance(mEvent.getStartTime());
                startTime.setTargetFragment(EventFragment.this, REQUEST_START_DATETIME);
                startTime.show(getFragmentManager(), DIALOG_TIME);
                break;
            case R.id.et_end_date:
                DatePickerFragment endDate = DatePickerFragment.newInstance(mEvent.getEndTime());
                endDate.setTargetFragment(EventFragment.this, REQUEST_END_DATETIME);
                endDate.show(getFragmentManager(), DIALOG_DATE);
                break;
            case R.id.et_end_time:
                TimePickerFragment endTime = TimePickerFragment.newInstance(mEvent.getEndTime());
                endTime.setTargetFragment(EventFragment.this, REQUEST_END_DATETIME);
                endTime.show(getFragmentManager(), DIALOG_TIME);
                break;
            case R.id.fab_confirm:
                attemptAdd();
                break;
        }
    }

    private void attemptAdd() {
        mEtTitle.setError(null);
        String title = mEtTitle.getEditText().getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(title)) {
            mEtTitle.setError(getString(R.string.error_field_required));
            focusView = mEtTitle;
            cancel = true;
        }

        if (!checkDateTime()) {
            focusView = mEtEndTime;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Category category = (Category) mSpnCategory.getSelectedItem();
            mEvent.setCategory(category.getId());
            mEvent.setTitle(mEtTitle.getEditText().getText().toString());
            mEvent.setNotes(mEtNotes.getEditText().getText().toString());
            getActivity().finish();
        }
    }

    private boolean checkDateTime() {
        if (mEvent.getEndTime().before(mEvent.getStartTime())) {
            Snackbar.make(getView(), this.getString(R.string.error_invalid_endTime), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateUI() {
        checkDateTime();
        mEtStartDate.setText(TimeUtils.cal2dateString(mEvent.getStartTime()));
        mEtStartTime.setText(TimeUtils.cal2timeString(mEvent.getStartTime()));
        mEtEndDate.setText(TimeUtils.cal2dateString(mEvent.getEndTime()));
        mEtEndTime.setText(TimeUtils.cal2timeString(mEvent.getEndTime()));
    }

    private static class CategoryAdapter extends BaseAdapter {
        private List<Category> mCategories;
        private LayoutInflater mInflater;

        public CategoryAdapter(List<Category> categories, Context context) {
            mCategories = categories;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mCategories.size();
        }

        @Override
        public Object getItem(int position) {
            return mCategories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_category, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mCategoryColor.getBackground().setTint(Color.parseColor(mCategories.get(position).getColor()));
            holder.mCategoryName.setText(mCategories.get(position).getTitle());
            return convertView;
        }

        static class ViewHolder {
            View view;
            Category mCategory;
            View mCategoryColor;
            TextView mCategoryName;

            ViewHolder(View view) {
                this.view = view;
                this.mCategoryColor = view.findViewById(R.id.v_color);
                this.mCategoryName = view.findViewById(R.id.tv_category_title);
            }
        }


    }
}
