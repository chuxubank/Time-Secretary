package com.termproject.misaka.timesecretary.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.termproject.misaka.timesecretary.base.BaseSingleFragmentActivity;
import com.termproject.misaka.timesecretary.controller.fragment.AnalysisFragment;

import java.util.Calendar;

public class AnalysisActivity extends BaseSingleFragmentActivity {

    private static final String EXTRA_START_DATE = "com.termproject.misaka.timesecretary.start_date";
    private static final String EXTRA_END_DATE = "com.termproject.misaka.timesecretary.end_date";

    public static Intent newIntent(Context packageContext, Calendar startDate, Calendar endDate) {
        Intent intent = new Intent(packageContext, AnalysisActivity.class);
        intent.putExtra(EXTRA_START_DATE, startDate);
        intent.putExtra(EXTRA_END_DATE, endDate);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        Calendar startDate = (Calendar) getIntent().getSerializableExtra(EXTRA_START_DATE);
        Calendar endDate = (Calendar) getIntent().getSerializableExtra(EXTRA_END_DATE);
        return AnalysisFragment.newInstance(startDate, endDate);
    }
}
