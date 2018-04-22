package com.termproject.misaka.timesecretary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.termproject.misaka.timesecretary.utils.TimeUtils;

import java.util.Calendar;

/**
 * @author misaka
 */
public class NewEventFragment extends Fragment implements View.OnClickListener {

    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_START_DATETIME = 0;
    private static final int REQUEST_END_DATETIME = 1;
    private Event mEvent;
    private EditText mEtStartDate;
    private EditText mEtStartTime;
    private EditText mEtEndDate;
    private EditText mEtEndTime;
    private View view;
    private FloatingActionButton mFabConfirm;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvent = new Event();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_event, container, false);
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
            mEvent.setStartTime(calendar);
            updateDateTime();
        } else if (requestCode == REQUEST_END_DATETIME) {
            Calendar calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATETIME);
            mEvent.setEndTime(calendar);
            updateDateTime();
        }
    }

    private void initView(View v) {

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
                startDate.setTargetFragment(NewEventFragment.this, REQUEST_START_DATETIME);
                startDate.show(getFragmentManager(), DIALOG_DATE);
                break;
            case R.id.et_start_time:
                TimePickerFragment startTime = TimePickerFragment.newInstance(mEvent.getStartTime());
                startTime.setTargetFragment(NewEventFragment.this, REQUEST_START_DATETIME);
                startTime.show(getFragmentManager(), DIALOG_TIME);
                break;
            case R.id.et_end_date:
                DatePickerFragment endDate = DatePickerFragment.newInstance(mEvent.getEndTime());
                endDate.setTargetFragment(NewEventFragment.this, REQUEST_END_DATETIME);
                endDate.show(getFragmentManager(), DIALOG_DATE);
                break;
            case R.id.et_end_time:
                TimePickerFragment endTime = TimePickerFragment.newInstance(mEvent.getEndTime());
                endTime.setTargetFragment(NewEventFragment.this, REQUEST_END_DATETIME);
                endTime.show(getFragmentManager(), DIALOG_TIME);
                break;
            case R.id.fab_confirm:

                break;
        }
    }

    private void checkDateTime() {
        if (mEvent.getEndTime().before(mEvent.getStartTime())) {
            Snackbar.make(getView(), this.getString(R.string.error_invalid_endTime), Snackbar.LENGTH_SHORT).show();
            mEvent.setEndTime(mEvent.getStartTime());
        }
    }


    private void updateDateTime() {
        checkDateTime();

        mEtStartDate.setText(TimeUtils.cal2dateString(mEvent.getStartTime()));
        mEtStartTime.setText(TimeUtils.cal2timeString(mEvent.getStartTime()));
        mEtEndDate.setText(TimeUtils.cal2dateString(mEvent.getEndTime()));
        mEtEndTime.setText(TimeUtils.cal2timeString(mEvent.getEndTime()));
    }
}
