package com.termproject.misaka.timesecretary.part;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.termproject.misaka.timesecretary.R;

import java.util.Calendar;

public class TimePickerFragment extends AppCompatDialogFragment {
    public static final String EXTRA_DATETIME = "com.termproject.misaka.timesecretary.datetime";
    private static final String ARG_DATETIME = "datetime";
    private TimePicker mTimePicker;
    private Calendar mCalendar;

    public static TimePickerFragment newInstance(Calendar calendar) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATETIME, calendar);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCalendar = (Calendar) getArguments().getSerializable(ARG_DATETIME);
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setIs24HourView(false);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minute);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = mTimePicker.getHour();
                        int minute = mTimePicker.getMinute();
                        mCalendar.set(Calendar.HOUR, hour);
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
        intent.putExtra(EXTRA_DATETIME, calendar);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
