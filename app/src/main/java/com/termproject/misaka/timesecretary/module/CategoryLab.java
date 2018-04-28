package com.termproject.misaka.timesecretary.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

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

    private CategoryLab(Context context) {
        mContext = context.getApplicationContext();
        mCategories = new ArrayList<>();
        mCategories.add(new Category("Life", "#FF5722"));
        mCategories.add(new Category("Work", "#9C27B0"));
        mCategories.add(new Category("Study", "#4CAF50"));
    }

    public void addCategory(Category c) {
        mCategories.add(c);
    }

    public void deleteCategory(Category c) {
        mCategories.remove(c);
    }

    public void clearNoTitle() {
        for (Category c : mCategories) {
            if (TextUtils.isEmpty(c.getTitle())) {
                mCategories.remove(c);
            }
        }
    }

    public int getPosition(UUID id) {
        return mCategories.indexOf(getCategory(id));
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
