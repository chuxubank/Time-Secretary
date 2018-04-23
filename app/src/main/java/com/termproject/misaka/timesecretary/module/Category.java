package com.termproject.misaka.timesecretary.module;

import java.util.UUID;

/**
 * @author misaka
 */
public class Category {
    private UUID mId;
    private String mName;
    private int mColor;

    public Category() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }
}
