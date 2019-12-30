package com.termproject.misaka.timesecretary.part;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.termproject.misaka.timesecretary.R;

import java.util.Calendar;

public class TimePickerFragment extends AppCompatDialogFragment {

    public static final String EXTRA_TIME = "com.termproject.misaka.timesecretary.time";
    private static final String ARG_TIME = "time";
    private TimePicker mTimePicker;
    private Calendar mCalendar;

    public static TimePickerFragment newInstance(Calendar calendar) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, calendar);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCalendar = (Calendar) getArguments().getSerializable(ARG_TIME);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setIs24HourView(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int hour = 0;
                        int minute = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            hour = mTimePicker.getHour();
                            minute = mTimePicker.getMinute();
                        } else {
                            hour = mTimePicker.getCurrentHour();
                            minute = mTimePicker.getCurrentMinute();
                        }
                        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        mCalendar.set(Calendar.MINUTE, minute);
                        sendResult(Activity.RESULT_OK, mCalendar);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Calendar calendar) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, calendar);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
