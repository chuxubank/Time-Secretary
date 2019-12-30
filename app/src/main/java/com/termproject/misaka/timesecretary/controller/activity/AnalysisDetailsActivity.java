package com.termproject.misaka.timesecretary.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.termproject.misaka.timesecretary.base.BaseSingleFragmentActivity;
import com.termproject.misaka.timesecretary.controller.fragment.AnalysisDetailsFragment;

import java.util.Calendar;
import java.util.UUID;

public class AnalysisDetailsActivity extends BaseSingleFragmentActivity {
    private static final String EXTRA_START_DATE = "com.termproject.misaka.timesecretary.start_date";
    private static final String EXTRA_END_DATE = "com.termproject.misaka.timesecretary.end_date";
    private static final String EXTRA_CATEGORY_ID = "com.termproject.misaka.timesecretary.category_id";

    public static Intent newIntent(Context packageContext, Calendar startDate, Calendar endDate, UUID categoryId) {
        Intent intent = new Intent(packageContext, AnalysisDetailsActivity.class);
        intent.putExtra(EXTRA_START_DATE, startDate);
        intent.putExtra(EXTRA_END_DATE, endDate);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        Calendar startDate = (Calendar) getIntent().getSerializableExtra(EXTRA_START_DATE);
        Calendar endDate = (Calendar) getIntent().getSerializableExtra(EXTRA_END_DATE);
        UUID categoryId = (UUID) getIntent().getSerializableExtra(EXTRA_CATEGORY_ID);
        return AnalysisDetailsFragment.newInstance(startDate, endDate, categoryId);
    }
}
