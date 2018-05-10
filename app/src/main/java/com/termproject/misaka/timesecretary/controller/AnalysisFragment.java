package com.termproject.misaka.timesecretary.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.Category;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.part.DatePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateCalendar;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateString;

public class AnalysisFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AnalysisFragment";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String ARG_START_DATE = "start_date";
    private static final String ARG_END_DATE = "end_date";
    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_END_DATE = 1;
    private PieChart mChart;
    private List<Category> mCategories;
    private CategoryLab mCategoryLab;
    private BottomNavigationView mNavigation;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private TextInputEditText mEtStartDate;
    private TextInputEditText mEtEndDate;

    public static AnalysisFragment newInstance(Calendar startDate, Calendar endDate) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_START_DATE, startDate);
        args.putSerializable(ARG_END_DATE, endDate);
        AnalysisFragment fragment = new AnalysisFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartDate = (Calendar) getArguments().getSerializable(ARG_START_DATE);
        mEndDate = (Calendar) getArguments().getSerializable(ARG_END_DATE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analysis, container, false);
        initView(v);
        updateUI();
        return v;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_day:
                    mStartDate = cal2dateCalendar(Calendar.getInstance());
                    mEndDate = cal2dateCalendar(Calendar.getInstance());
                    mEndDate.add(Calendar.DATE, 1);
                    updateUI();
                    return true;
                case R.id.navigation_week:
                    mStartDate = cal2dateCalendar(Calendar.getInstance());
                    mStartDate.add(Calendar.DATE, mStartDate.getFirstDayOfWeek() - mStartDate.get(Calendar.DAY_OF_WEEK));
                    mEndDate = cal2dateCalendar(Calendar.getInstance());
                    mEndDate.add(Calendar.DATE, mEndDate.getFirstDayOfWeek() - mEndDate.get(Calendar.DAY_OF_WEEK) + 7);
                    mEndDate.add(Calendar.DATE, 1);
                    updateUI();
                    return true;
                case R.id.navigation_month:
                    mStartDate = cal2dateCalendar(Calendar.getInstance());
                    mStartDate.set(Calendar.DAY_OF_MONTH, mEndDate.getActualMinimum(Calendar.DAY_OF_MONTH));
                    mEndDate = cal2dateCalendar(Calendar.getInstance());
                    mEndDate.set(Calendar.DAY_OF_MONTH, mEndDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                    mEndDate.add(Calendar.DATE, 1);
                    updateUI();
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    private void initView(View v) {
        mCategoryLab = CategoryLab.get(getActivity());
        mNavigation = v.findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mChart = v.findViewById(R.id.chart);
        mEtStartDate = v.findViewById(R.id.et_start_date);
        mEtStartDate.setOnClickListener(this);
        mEtEndDate = v.findViewById(R.id.et_end_date);
        mEtEndDate.setOnClickListener(this);

        mChart.getDescription().setEnabled(false);
        mChart.setUsePercentValues(true);
        mChart.setExtraOffsets(5, 10, 5, 5);
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.et_start_date:
                DatePickerFragment startDate = DatePickerFragment.newInstance(mStartDate);
                startDate.setTargetFragment(this, REQUEST_START_DATE);
                startDate.show(getFragmentManager(), DIALOG_DATE);
                break;
            case R.id.et_end_date:
                DatePickerFragment endDate = DatePickerFragment.newInstance(mEndDate);
                endDate.setTargetFragment(this, REQUEST_END_DATE);
                endDate.show(getFragmentManager(), DIALOG_DATE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_START_DATE:
                mStartDate = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                if (!isValidDate()) {
                    mEndDate.setTime(mStartDate.getTime());
                }
                break;

            case REQUEST_END_DATE:
                mEndDate = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                if (!isValidDate()) {
                    mStartDate.setTime(mEndDate.getTime());
                }
                break;

            default:
                break;
        }
        updateUI();
    }

    private void updateUI() {
        mCategories = CategoryLab.get(getActivity()).getCategories();

        mEtStartDate.setText(cal2dateString(mStartDate));
        mEtEndDate.setText(cal2dateString(mEndDate));

        List<Integer> colors = new ArrayList<>();
        List<PieEntry> entries = new ArrayList<>();
        for (Category c : mCategories) {
            entries.add(new PieEntry(mCategoryLab.getDuration(c.getId(), mStartDate, mEndDate), c.getTitle()));
            colors.add(Color.parseColor(c.getColor()));
        }
        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        mChart.setData(data);
        mChart.invalidate();
    }

    private boolean isValidDate() {
        return mStartDate.before(mEndDate);
    }
}
