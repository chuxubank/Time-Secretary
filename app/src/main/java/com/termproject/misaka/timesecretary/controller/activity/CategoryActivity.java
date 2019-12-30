package com.termproject.misaka.timesecretary.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.termproject.misaka.timesecretary.base.BaseSingleFragmentActivity;
import com.termproject.misaka.timesecretary.controller.fragment.CategoryFragment;

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
