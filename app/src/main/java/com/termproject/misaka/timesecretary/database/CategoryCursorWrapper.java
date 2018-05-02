package com.termproject.misaka.timesecretary.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.termproject.misaka.timesecretary.database.CategoryDbSchema.CategoryTable.Cols;
import com.termproject.misaka.timesecretary.module.Category;

import java.util.UUID;

public class CategoryCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CategoryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Category getCategory() {
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String title = getString(getColumnIndex(Cols.TITLE));
        String color = getString(getColumnIndex(Cols.COLOR));

        Category category = new Category(UUID.fromString(uuidString));
        category.setTitle(title);
        category.setColor(color);
        return category;
    }
}
