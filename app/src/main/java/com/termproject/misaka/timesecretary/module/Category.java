package com.termproject.misaka.timesecretary.module;

import java.util.UUID;

/**
 * @author misaka
 */
public class Category {
    private UUID mId;
    private String mTitle;
    private int mColor;

    public Category() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }
}
