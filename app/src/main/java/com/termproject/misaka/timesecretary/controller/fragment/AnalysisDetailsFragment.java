package com.termproject.misaka.timesecretary.controller.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Entity;
import com.termproject.misaka.timesecretary.module.EventLab;
import com.termproject.misaka.timesecretary.module.TaskLab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.getDuration;

public class AnalysisDetailsFragment extends Fragment {

    private static final String TAG = "AnalysisDetailsFragment";
    private static final String ARG_START_DATE = "start_date";
    private static final String ARG_END_DATE = "end_date";
    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String ARG_SORT_BY_DURATION = "sort_by_duration";
    private HorizontalBarChart mChart;
    private List<Entity> mEntities;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private UUID mCategoryId;

    public static Fragment newInstance(Calendar startDate, Calendar endDate, UUID categoryId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_START_DATE, startDate);
        args.putSerializable(ARG_END_DATE, endDate);
        args.putSerializable(ARG_CATEGORY_ID, categoryId);
        AnalysisDetailsFragment fragment = new AnalysisDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_analysis_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_duration:
                updateUI(ARG_SORT_BY_DURATION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartDate = (Calendar) getArguments().getSerializable(ARG_START_DATE);
        mEndDate = (Calendar) getArguments().getSerializable(ARG_END_DATE);
        mCategoryId = (UUID) getArguments().getSerializable(ARG_CATEGORY_ID);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analysis_details, container, false);
        initView(v);
        updateUI(null);
        return v;
    }

    private void initView(View v) {
        setHasOptionsMenu(true);
        mChart = v.findViewById(R.id.chart);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setAutoScaleMinMaxEnabled(true);
//        mChart.getAxisLeft().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(1440);
        xAxis.setTextSize(12f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new MyLeftAxisValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawTopYLabelEntry(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(30f);
        leftAxis.setTextSize(12f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(15 * 60 * 1000f);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setValueFormatter(new MyRightAxisValueFormatter());
        rightAxis.setDrawGridLines(true);
        rightAxis.setDrawTopYLabelEntry(true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setSpaceTop(30f);
        rightAxis.setTextSize(12f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setGranularity(60 * 60 * 1000f);
        rightAxis.setGranularityEnabled(true);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(10f);
        l.setTextSize(12f);
        l.setXEntrySpace(4f);
    }

    private void updateUI(String sortOrder) {
        List<BarEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        List<LegendEntry> legendEntries = new ArrayList<>();
        String[] titles;
        Set<myLegend> myLegends = new HashSet<>();
        mEntities = new ArrayList<>();
        mEntities.addAll(EventLab.get(getActivity()).getEvents());
        mEntities.addAll(TaskLab.get(getActivity()).getTasks());
        Iterator<Entity> iterator = mEntities.iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();
            if (mCategoryId != null && !e.getCategory().equals(mCategoryId)) {
                iterator.remove();
            }
        }
        if (sortOrder != null) {
            switch (sortOrder) {
                case ARG_SORT_BY_DURATION:
                    Collections.sort(mEntities, new Comparator<Entity>() {
                        @Override
                        public int compare(Entity o1, Entity o2) {
                            return (int) (o1.getDuration() - o2.getDuration());
                        }
                    });
                    break;
            }
        }
        titles = new String[mEntities.size()];
        for (int i = 0; i < mEntities.size(); i++) {
            long duration = getDuration(mEntities.get(i), mStartDate, mEndDate);
            int color = Color.parseColor(CategoryLab.get(getActivity()).getCategory(mEntities.get(i).getCategory()).getColor());
            String categoryTitle = CategoryLab.get(getActivity()).getCategory(mEntities.get(i).getCategory()).getTitle();
            String title = mEntities.get(i).getTitle();
            if (duration > 0) {
                entries.add(new BarEntry(i, duration, mEntities.get(i).getNotes()));
                colors.add(color);
            }
            boolean shouldAdd = duration > 0;
            for (myLegend l : myLegends) {
                if (l.color == color) {
                    shouldAdd = false;
                    break;
                }
            }
            if (shouldAdd)
                myLegends.add(new myLegend(color, categoryTitle));
            titles[i] = title;
        }
        for (myLegend l : myLegends) {
            legendEntries.add(new LegendEntry(l.label, Legend.LegendForm.SQUARE, 12f, 12f, null, l.color));
        }
        BarDataSet dataSet = new BarDataSet(entries, null);
        dataSet.setColors(colors);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);
        data.setValueTextSize(12f);
        data.setValueFormatter(new MyValueFormatter());
        mChart.setData(data);
        mChart.setFitBars(true);
        mChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter(titles));
        mChart.getLegend().setCustom(legendEntries);
        mChart.invalidate();
        mChart.animateY(1000);
    }

    private class myLegend {
        int color;
        String label;

        public myLegend(int color, String label) {
            this.color = color;
            this.label = label;
        }
    }

    public class MyValueFormatter implements IValueFormatter {

        private int timeFormatter(float time) {
            return (int) time / 60 / 1000;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return timeFormatter(value) + " m";
        }
    }

    public class MyLeftAxisValueFormatter implements IAxisValueFormatter {

        private int timeFormatter(float time) {
            return (int) time / 60 / 1000 / 15 * 15;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return timeFormatter(value) + " m";
        }
    }

    public class MyRightAxisValueFormatter implements IAxisValueFormatter {

        private int timeFormatter(float time) {
            return (int) time / 60 / 60 / 1000;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return timeFormatter(value) + " h";
        }
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }
}
