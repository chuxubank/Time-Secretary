package com.termproject.misaka.timesecretary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;

public class AddFragment extends Fragment implements View.OnClickListener {

    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_START_DATETIME = 0;
    private static final int REQUEST_END_DATETIME = 1;
    private Event mEvent;
    private EditText mEtStartDate;
    private EditText mEtStartTime;
    private EditText mEtEndDate;
    private EditText mEtEndTime;
    private AppCompatCheckedTextView mCtvAsTask;
    private View view;
    private FloatingActionButton mFabConfirm;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvent = new Event();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);
        initView(v);
        updateDateTime();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_START_DATETIME) {
            Calendar calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATETIME);
            mEvent.setStartDate(calendar);
            updateDateTime();
        } else if (requestCode == REQUEST_END_DATETIME) {
            Calendar calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATETIME);
            mEvent.setEndDate(calendar);
            updateDateTime();
        }
    }

    private void initView(View v) {

        mEtStartDate = (EditText) v.findViewById(R.id.et_start_date);
        mEtStartDate.setOnClickListener(this);
        mEtStartTime = (EditText) v.findViewById(R.id.et_start_time);
        mEtStartTime.setOnClickListener(this);
        mEtEndDate = (EditText) v.findViewById(R.id.et_end_date);
        mEtEndDate.setOnClickListener(this);
        mEtEndTime = (EditText) v.findViewById(R.id.et_end_time);
        mEtEndTime.setOnClickListener(this);
        mCtvAsTask = (AppCompatCheckedTextView) v.findViewById(R.id.ctv_as_task);
        mCtvAsTask.setOnClickListener(this);
        mFabConfirm = (FloatingActionButton) v.findViewById(R.id.fab_confirm);
        mFabConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.et_start_date:
                DatePickerFragment startDate = DatePickerFragment.newInstance(mEvent.getStartDate());
                startDate.setTargetFragment(AddFragment.this, REQUEST_START_DATETIME);
                startDate.show(getFragmentManager(), DIALOG_DATE);
                break;
            case R.id.et_start_time:
                TimePickerFragment startTime = TimePickerFragment.newInstance(mEvent.getStartDate());
                startTime.setTargetFragment(AddFragment.this, REQUEST_START_DATETIME);
                startTime.show(getFragmentManager(), DIALOG_TIME);
                break;
            case R.id.et_end_date:
                DatePickerFragment endDate = DatePickerFragment.newInstance(mEvent.getEndDate());
                endDate.setTargetFragment(AddFragment.this, REQUEST_END_DATETIME);
                endDate.show(getFragmentManager(), DIALOG_DATE);
                break;
            case R.id.et_end_time:
                TimePickerFragment endTime = TimePickerFragment.newInstance(mEvent.getEndDate());
                endTime.setTargetFragment(AddFragment.this, REQUEST_END_DATETIME);
                endTime.show(getFragmentManager(), DIALOG_TIME);
                break;
            case R.id.ctv_as_task:
                mCtvAsTask.toggle();
                break;
            case R.id.fab_confirm:

                break;
        }
    }

    private void checkDateTime() {
        if (mEvent.getEndDate().before(mEvent.getStartDate())) {
            Snackbar.make(getView(), this.getString(R.string.error_invalid_endTime), Snackbar.LENGTH_SHORT).show();
            mEvent.setEndDate(mEvent.getStartDate());
        }
    }


    private void updateDateTime() {
        checkDateTime();
        mEtStartDate.setText(DateFormat.getDateInstance().format(mEvent.getStartDate().getTime()));
        mEtStartTime.setText(DateFormat.getTimeInstance().format(mEvent.getStartDate().getTime()));
        mEtEndDate.setText(DateFormat.getDateInstance().format(mEvent.getEndDate().getTime()));
        mEtEndTime.setText(DateFormat.getTimeInstance().format(mEvent.getEndDate().getTime()));
    }
}
