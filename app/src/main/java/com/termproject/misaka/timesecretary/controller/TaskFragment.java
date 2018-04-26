package com.termproject.misaka.timesecretary.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.Category;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Task;
import com.termproject.misaka.timesecretary.module.TaskLab;
import com.termproject.misaka.timesecretary.part.DatePickerFragment;
import com.termproject.misaka.timesecretary.utils.TimeUtils;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * @author misaka
 */
public class TaskFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_TASK_ID = "task_id";

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DEFER_UNTIL = 1;
    private static final int REQUEST_DEADLINE = 2;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task, container, false);
        initView(v);
        updateUI();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DEFER_UNTIL) {
            Calendar calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATETIME);
            mTask.setDeferUntil(calendar);
            updateUI();
        } else if (requestCode == REQUEST_DEADLINE) {
            Calendar calendar = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATETIME);
            mTask.setDeadline(calendar);
            updateUI();
        }
    }

    private void initView(View v) {

        mFabConfirm = v.findViewById(R.id.fab_confirm);
        mFabConfirm.setOnClickListener(this);
        mEtTitle = v.findViewById(R.id.et_title);
        mEtNotes = v.findViewById(R.id.et_notes);
        mSpnCategory = v.findViewById(R.id.spn_category);
        CategoryLab categoryLab = CategoryLab.get(getActivity());
        List<Category> categories = categoryLab.getCategories();
        mCategoryAdapter = new CategoryAdapter(categories, getActivity());
        mSpnCategory.setAdapter(mCategoryAdapter);
        mToolbar = v.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        mEtTitle = v.findViewById(R.id.et_title);
        EditText editText = mEtTitle.getEditText();
        editText.addTextChangedListener(new TextWatcher() {
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
        mEtDeferUntil = v.findViewById(R.id.et_defer_until);
        mEtDeadline = v.findViewById(R.id.et_deadline);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.et_defer_until:
                DatePickerFragment startDate = DatePickerFragment.newInstance(mTask.getStartTime());
                startDate.setTargetFragment(TaskFragment.this, REQUEST_DEFER_UNTIL);
                startDate.show(getFragmentManager(), DIALOG_DATE);
                break;
            case R.id.et_deadline:
                DatePickerFragment endDate = DatePickerFragment.newInstance(mTask.getEndTime());
                endDate.setTargetFragment(TaskFragment.this, REQUEST_DEADLINE);
                endDate.show(getFragmentManager(), DIALOG_DATE);
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
            getActivity().finish();
        }
    }

    private boolean checkDateTime() {
        if (mTask.getDeadline().before(mTask.getDeferUntil())) {
            Snackbar.make(getView(), this.getString(R.string.error_invalid_endTime), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateUI() {
        checkDateTime();
        mEtDeferUntil.setText(TimeUtils.cal2dateString(mTask.getDeferUntil()));
        mEtDeadline.setText(TimeUtils.cal2dateString(mTask.getDeadline()));
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                this.mCategoryName = view.findViewById(R.id.tv_title);
            }
        }
    }
}
