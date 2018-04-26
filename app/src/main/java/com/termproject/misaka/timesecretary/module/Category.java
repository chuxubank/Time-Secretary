package com.termproject.misaka.timesecretary.module;

import java.util.UUID;

/**
 * @author misaka
 */
public class Category {
    private UUID mId;
    private String mTitle;
    private String mColor;

    public Category() {
        mId = UUID.randomUUID();
        mColor = "#66CCFF";
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

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }
}
