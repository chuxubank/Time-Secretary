package com.termproject.misaka.timesecretary.controller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private List<Category> mCategories;
    private LayoutInflater mInflater;

    public CategoryAdapter(List<Category> categories, Context context) {
        mCategories = categories;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_category, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mVColor.getBackground().setTint(Color.parseColor(mCategories.get(position).getColor()));
        holder.mTvCategoryTitle.setText(mCategories.get(position).getTitle());
        return convertView;
    }

    static class ViewHolder {
        View view;
        View mVColor;
        TextView mTvCategoryTitle;

        ViewHolder(View view) {
            this.view = view;
            this.mVColor = view.findViewById(R.id.v_color);
            this.mTvCategoryTitle = view.findViewById(R.id.tv_category_title);
        }
    }
}
