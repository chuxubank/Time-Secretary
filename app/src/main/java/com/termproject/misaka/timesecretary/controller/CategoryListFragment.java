package com.termproject.misaka.timesecretary.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.Category;
import com.termproject.misaka.timesecretary.module.CategoryLab;

import java.util.List;

public class CategoryListFragment extends Fragment {

    private static final String TAG = "CategoryListFragment";
    private RecyclerView mRvCategory;
    private CategoryAdapter mCategoryAdapter;
    private MainActivity mActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_list, container, false);
        Log.i(TAG, "onCreateView");
        initView(v);
        updateUI();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        updateUI();
    }

    private void initView(View v) {
        mActivity = (MainActivity) getActivity();
        mRvCategory = v.findViewById(R.id.rv_category);
        mRvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void updateUI() {
        CategoryLab categoryLab = CategoryLab.get(getActivity());
        categoryLab.clearNoTitle();
        List<Category> categories = categoryLab.getCategories();
        if (mCategoryAdapter == null) {
            mCategoryAdapter = new CategoryAdapter(categories);
        } else {
            mCategoryAdapter.setCategories(categories);
            mCategoryAdapter.notifyDataSetChanged();
        }
        mRvCategory.setAdapter(mCategoryAdapter);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Category mCategory;
        private View mVColor;
        private TextView mTvCategoryTitle;

        public CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_category, parent, false));
            itemView.setOnClickListener(this);
            mVColor = itemView.findViewById(R.id.v_color);
            mTvCategoryTitle = itemView.findViewById(R.id.tv_category_title);
        }

        public void bind(Category category) {
            mCategory = category;
            mVColor.getBackground().setTint(Color.parseColor(mCategory.getColor()));
            mTvCategoryTitle.setText(mCategory.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = CategoryActivity.newIntent(getActivity(), mCategory.getId());
            startActivity(intent);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private List<Category> mCategories;

        public CategoryAdapter(List<Category> categories) {
            mCategories = categories;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CategoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Category category = mCategories.get(position);
            holder.bind(category);
        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }

        public void setCategories(List<Category> categories) {
            mCategories = categories;
        }
    }
}
