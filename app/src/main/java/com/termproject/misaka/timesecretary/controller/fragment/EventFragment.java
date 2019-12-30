package com.termproject.misaka.timesecretary.controller.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.controller.adapter.CategoryAdapter;
import com.termproject.misaka.timesecretary.module.Category;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.module.EventLab;
import com.termproject.misaka.timesecretary.part.DatePickerFragment;
import com.termproject.misaka.timesecretary.part.TimePickerFragment;

import java.util.Calendar;
import java.util.UUID;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateString;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2timeString;

/**
 * @author misaka
 */
public class EventFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_EVENT_ID = "event_id";

    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_START_TIME = 1;
    private static final int REQUEST_END_DATE = 2;
    private static final int REQUEST_END_TIME = 3;
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
        for (int i = 0; i < mCategoryAdapter.getCount(); i++) {
            Category category = (Category) mCategoryAdapter.getItem(i);
            if (category.getId().equals(mEvent.getCategory())) {
                mSpnCategory.setSelection(i, true);
                break;
            }
        }
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
                startDate.setTargetFragment(EventFragment.this, REQUEST_START_DATE);
                startDate.show(getFragmentManager(), DIALOG_DATE);
                break;
            case R.id.et_start_time:
                TimePickerFragment startTime = TimePickerFragment.newInstance(mEvent.getStartTime());
                startTime.setTargetFragment(EventFragment.this, REQUEST_START_TIME);
                startTime.show(getFragmentManager(), DIALOG_TIME);
                break;
            case R.id.et_end_date:
                DatePickerFragment endDate = DatePickerFragment.newInstance(mEvent.getEndTime());
                endDate.setTargetFragment(EventFragment.this, REQUEST_END_DATE);
                endDate.show(getFragmentManager(), DIALOG_DATE);
                break;
            case R.id.et_end_time:
                TimePickerFragment endTime = TimePickerFragment.newInstance(mEvent.getEndTime());
                endTime.setTargetFragment(EventFragment.this, REQUEST_END_TIME);
                endTime.show(getFragmentManager(), DIALOG_TIME);
                break;
            case R.id.fab_confirm:
                attemptUpdate();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Calendar calendar;
        switch (requestCode) {
            case REQUEST_START_DATE:
                calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mEvent.getStartTime().set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                if (!isValidDateTime()) {
                    mEvent.getEndTime().setTime(mEvent.getStartTime().getTime());
                    mEvent.getEndTime().add(Calendar.HOUR, 1);
                }
                break;

            case REQUEST_START_TIME:
                calendar = (Calendar) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
                mEvent.setStartTime(calendar);
                if (!isValidDateTime()) {
                    mEvent.getEndTime().setTime(mEvent.getStartTime().getTime());
                    mEvent.getEndTime().add(Calendar.HOUR, 1);
                }
                break;

            case REQUEST_END_DATE:
                calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mEvent.getEndTime().set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                if (!isValidDateTime()) {
                    mEvent.getStartTime().setTime(mEvent.getEndTime().getTime());
                    mEvent.getStartTime().add(Calendar.HOUR, -1);
                }
                break;

            case REQUEST_END_TIME:
                calendar = (Calendar) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
                mEvent.setEndTime(calendar);
                if (!isValidDateTime()) {
                    mEvent.getStartTime().setTime(mEvent.getEndTime().getTime());
                    mEvent.getStartTime().add(Calendar.HOUR, -1);
                }
                break;

            default:
                break;
        }
        updateUI();
    }

    private void attemptUpdate() {
        mEtTitle.setError(null);
        String title = mEtTitle.getEditText().getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(title)) {
            mEtTitle.setError(getString(R.string.error_field_required));
            focusView = mEtTitle;
            cancel = true;
        }

        if (!isValidDateTime()) {
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
            EventLab.get(getActivity()).updateEvent(mEvent);
            getActivity().finish();
        }
    }

    private boolean isValidDateTime() {
        return mEvent.getStartTime().before(mEvent.getEndTime());
    }

    private void updateUI() {
        mEtStartDate.setText(cal2dateString(mEvent.getStartTime()));
        mEtStartTime.setText(cal2timeString(mEvent.getStartTime()));
        mEtEndDate.setText(cal2dateString(mEvent.getEndTime()));
        mEtEndTime.setText(cal2timeString(mEvent.getEndTime()));
    }
}
