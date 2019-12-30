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
import com.termproject.misaka.timesecretary.module.Task;
import com.termproject.misaka.timesecretary.module.TaskLab;
import com.termproject.misaka.timesecretary.part.DatePickerFragment;

import java.util.Calendar;
import java.util.UUID;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateString;

/**
 * @author misaka
 */
public class TaskFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_TASK_ID = "task_id";

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DEFER_UNTIL = 0;
    private static final int REQUEST_DEADLINE = 1;
    private Task mTask;
    private FloatingActionButton mFabConfirm;
    private Spinner mSpnCategory;
    private CategoryAdapter mCategoryAdapter;
    private Toolbar mToolbar;
    private TextInputLayout mEtTitle;
    private TextInputLayout mEtNotes;
    private EditText mEtDeferUntil;
    private EditText mEtDeadline;

    public static TaskFragment newInstance(UUID taskId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        mTask = TaskLab.get(getActivity()).getTask(taskId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task, container, false);
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
                                    TaskLab.get(getActivity()).deleteTask(mTask);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .create().show();
                } else {
                    getActivity().finish();
                }
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
        mEtTitleEditText.setText(mTask.getTitle());
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
        mEtNotesEditText.setText(mTask.getNotes());
        mSpnCategory = v.findViewById(R.id.spn_category);
        CategoryLab categoryLab = CategoryLab.get(getActivity());
        mCategoryAdapter = new CategoryAdapter(categoryLab.getCategories(), getActivity());
        mSpnCategory.setAdapter(mCategoryAdapter);
        for (int i = 0; i < mCategoryAdapter.getCount(); i++) {
            Category category = (Category) mCategoryAdapter.getItem(i);
            if (category.getId().equals(mTask.getCategory())) {
                mSpnCategory.setSelection(i, true);
                break;
            }
        }
        mFabConfirm = v.findViewById(R.id.fab_confirm);
        mFabConfirm.setOnClickListener(this);
        mEtDeferUntil = v.findViewById(R.id.et_defer_until);
        mEtDeferUntil.setOnClickListener(this);
        mEtDeadline = v.findViewById(R.id.et_deadline);
        mEtDeadline.setOnClickListener(this);
    }

    private void updateUI() {
        mEtDeferUntil.setText(cal2dateString(mTask.getDeferUntil()));
        mEtDeadline.setText(cal2dateString(mTask.getDeadline()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.et_defer_until:
                DatePickerFragment startDate = DatePickerFragment.newInstance(mTask.getDeferUntil());
                startDate.setTargetFragment(TaskFragment.this, REQUEST_DEFER_UNTIL);
                startDate.show(getFragmentManager(), DIALOG_DATE);
                break;
            case R.id.et_deadline:
                DatePickerFragment endDate = DatePickerFragment.newInstance(mTask.getDeadline());
                endDate.setTargetFragment(TaskFragment.this, REQUEST_DEADLINE);
                endDate.show(getFragmentManager(), DIALOG_DATE);
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
        if (requestCode == REQUEST_DEFER_UNTIL) {
            Calendar calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mTask.setDeferUntil(calendar);
            if (!isValidDate()) {
                mTask.getDeadline().setTime(mTask.getDeferUntil().getTime());
            }
        } else if (requestCode == REQUEST_DEADLINE) {
            Calendar calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mTask.setDeadline(calendar);
            if (!isValidDate()) {
                mTask.getDeferUntil().setTime(mTask.getDeadline().getTime());
            }
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

        if (!isValidDate()) {
            focusView = mEtDeadline;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Category category = (Category) mSpnCategory.getSelectedItem();
            mTask.setCategory(category.getId());
            mTask.setTitle(mEtTitle.getEditText().getText().toString());
            mTask.setNotes(mEtNotes.getEditText().getText().toString());
            TaskLab.get(getActivity()).updateTask(mTask);
            getActivity().finish();
        }
    }

    private boolean isValidDate() {
        return !mTask.getDeferUntil().after(mTask.getDeadline());
    }

}
