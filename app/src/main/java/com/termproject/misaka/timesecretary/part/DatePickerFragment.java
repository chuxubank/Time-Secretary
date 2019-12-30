package com.termproject.misaka.timesecretary.part;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.termproject.misaka.timesecretary.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerFragment extends AppCompatDialogFragment {

    public static final String EXTRA_DATE = "com.termproject.misaka.timesecretary.date";
    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Calendar calendar) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, calendar);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = (Calendar) getArguments().getSerializable(ARG_DATE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        mDatePicker = v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Calendar calendar = new GregorianCalendar(year, month, day);
                        sendResult(Activity.RESULT_OK, calendar);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Calendar calendar) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, calendar);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
