package com.termproject.misaka.timesecretary.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.termproject.misaka.timesecretary.base.BaseSingleFragmentActivity;

import java.util.UUID;

public class CategoryActivity extends BaseSingleFragmentActivity {

    private static final String EXTRA_CATEGORY_ID = "com.termproject.misaka.timesecretary.category_id";

    public static Intent newIntent(Context packageContext, UUID categoryId) {
        Intent intent = new Intent(packageContext, CategoryActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        UUID categoryId = (UUID) getIntent().getSerializableExtra(EXTRA_CATEGORY_ID);
        return CategoryFragment.newInstance(categoryId);
    }
}
