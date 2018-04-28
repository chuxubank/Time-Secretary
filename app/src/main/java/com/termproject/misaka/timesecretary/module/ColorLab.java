package com.termproject.misaka.timesecretary.module;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ColorLab {
    private static ColorLab sColorLab;
    private Set<String> mColors;
    private Context mContext;
    private CategoryLab mCategoryLab;
    private List<Category> mCategories;

    private ColorLab(Context context) {
        mContext = context.getApplicationContext();
        mCategoryLab = CategoryLab.get(context);
        mColors = new TreeSet<>();
        updateColor();
    }

    public static ColorLab get(Context context) {
        if (sColorLab == null) {
            sColorLab = new ColorLab(context);
        }
        return sColorLab;
    }

    public void updateColor() {
        mCategories = mCategoryLab.getCategories();
        for (Category c : mCategories) {
            if (c.getColor() != null) {
                mColors.add(c.getColor());
            }
        }
    }

    public List<String> getColors() {
        return new ArrayList<>(mColors);
    }
}
