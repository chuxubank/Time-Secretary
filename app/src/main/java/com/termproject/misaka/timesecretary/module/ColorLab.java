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
        mColors.add("#F44336");
        mColors.add("#FFEB3B");
        mColors.add("#4CAF50");
        mColors.add("#2196F3");
        mColors.add("#9C27B0");
        mColors.add("#795548");
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
