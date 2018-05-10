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
import com.termproject.misaka.timesecretary.module.Task;
import com.termproject.misaka.timesecretary.module.TaskLab;

import java.util.Calendar;
import java.util.UUID;

public class TaskTimeFragment extends AppCompatDialogFragment {
    public static final String EXTRA_TASK_ID = "com.termproject.misaka.timesecretary.task_id";
    private static final String ARG_TASK_ID = "task_id";
    private Task mTask;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private TimePicker mTpStartTime;
    private TimePicker mTpEndTime;

    public static TaskTimeFragment newInstance(UUID taskId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        TaskTimeFragment fragment = new TaskTimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        mTask = TaskLab.get(getActivity()).getTask(taskId);

        mStartTime = mTask.getStartTime();
        mEndTime = mTask.getEndTime();

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_task_time, null);

        mTpStartTime = v.findViewById(R.id.tp_start_time);
        mTpStartTime.setIs24HourView(false);
        mTpStartTime.setHour(mStartTime.get(Calendar.HOUR));
        mTpStartTime.setMinute(mStartTime.get(Calendar.MINUTE));

        mTpEndTime = v.findViewById(R.id.tp_end_time);
        mTpEndTime.setIs24HourView(false);
        mTpEndTime.setHour(mEndTime.get(Calendar.HOUR));
        mTpEndTime.setMinute(mEndTime.get(Calendar.MINUTE));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_CANCELED, null);
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mStartTime.set(Calendar.HOUR, mTpStartTime.getHour());
                        mStartTime.set(Calendar.MINUTE, mTpStartTime.getMinute());
                        mEndTime.set(Calendar.HOUR, mTpEndTime.getHour());
                        mEndTime.set(Calendar.MINUTE, mTpEndTime.getMinute());
                        mTask.setStartTime(mStartTime);
                        mTask.setEndTime(mEndTime);
                        mTask.setChecked(true);
                        TaskLab.get(getActivity()).updateTask(mTask);
                        sendResult(Activity.RESULT_OK, mTask.getId());
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, UUID id) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TASK_ID, id);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
