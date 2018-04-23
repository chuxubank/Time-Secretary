package com.termproject.misaka.timesecretary.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;

import com.termproject.misaka.timesecretary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author misaka
 */
public class CategoryLab {
    private static CategoryLab sCategoryLab;
    private List<Category> mCategories;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private int mSize = 3;

    private CategoryLab(Context context) {
        mContext = context.getApplicationContext();
        mCategories = new ArrayList<>();
        for (int i = 1; i <= mSize; i++) {
            Category category = new Category();
            category.setName("Category #" + i);
            category.setColor(ContextCompat.getColor(mContext, R.color.test_color));
            mCategories.add(category);
        }
    }

    public static CategoryLab get(Context context) {
        if (sCategoryLab == null) {
            sCategoryLab = new CategoryLab(context);
        }
        return sCategoryLab;
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public Category getCategory(UUID id) {
        for (Category category : mCategories) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }
}
