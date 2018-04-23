package com.termproject.misaka.timesecretary.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
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
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.part.DatePickerFragment;
import com.termproject.misaka.timesecretary.part.TimePickerFragment;
import com.termproject.misaka.timesecretary.utils.TimeUtils;

import java.util.Calendar;
import java.util.List;

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
    private TextInputEditText mEtTitle;
    private TextInputEditText mEtNotes;
    private Spinner mSpnCategory;
    private CategoryAdapter mCategoryAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvent = new Event();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);
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
        mEtTitle = v.findViewById(R.id.et_title);
        mEtNotes = v.findViewById(R.id.et_notes);
        mSpnCategory = v.findViewById(R.id.spn_category);
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

    private void updateUI() {
        checkDateTime();
        mEtStartDate.setText(TimeUtils.cal2dateString(mEvent.getStartTime()));
        mEtStartTime.setText(TimeUtils.cal2timeString(mEvent.getStartTime()));
        mEtEndDate.setText(TimeUtils.cal2dateString(mEvent.getEndTime()));
        mEtEndTime.setText(TimeUtils.cal2timeString(mEvent.getEndTime()));
        CategoryLab categoryLab = CategoryLab.get(getActivity());
        List<Category> categories = categoryLab.getCategories();
        mCategoryAdapter = new CategoryAdapter(categories, getActivity());
        mSpnCategory.setAdapter(mCategoryAdapter);

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
        @SuppressLint("ResourceAsColor")
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
            holder.mCategoryColor.getBackground().setTint(mCategories.get(position).getColor());
            holder.mCategoryName.setText(mCategories.get(position).getName());
            return convertView;
        }

        static class ViewHolder {
            Category mCategory;
            View view;
            View mCategoryColor;
            TextView mCategoryName;

            ViewHolder(View view) {
                this.view = view;
                this.mCategoryColor = view.findViewById(R.id.category_color);
                this.mCategoryName = view.findViewById(R.id.category_name);
            }
        }


    }
}