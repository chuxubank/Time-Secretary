package com.termproject.misaka.timesecretary.controller;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.Category;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.ColorLab;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class CategoryFragment extends Fragment {

    private static final String ARG_CATEGORY_ID = "category_id";
    private Category mCategory;
    private Toolbar mToolbar;
    private TextInputLayout mEtTitle;
    private TextInputLayout mEtColor;
    private RecyclerView mRvColor;
    private FloatingActionButton mFabConfirm;
    private ColorAdapter mColorAdapter;

    public static CategoryFragment newInstance(UUID categoryId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORY_ID, categoryId);
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID category_id = (UUID) getArguments().getSerializable(ARG_CATEGORY_ID);
        mCategory = CategoryLab.get(getActivity()).getCategory(category_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        initView(v);
        updateUI();
        return v;
    }

    private void initView(View v) {
        mToolbar = v.findViewById(R.id.toolbar);
        mToolbar = v.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mEtTitle.getEditText().getText().toString())) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(android.R.string.dialog_alert_title)
                            .setMessage(R.string.alert_delete)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CategoryLab.get(getActivity()).deleteCategory(mCategory);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .create().show();
                } else {
                    getActivity().finish();
                }
            }
        });
        mEtTitle = v.findViewById(R.id.et_title);
        EditText mEtTitleEditText = mEtTitle.getEditText();
        mEtTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mEtTitle.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEtColor = v.findViewById(R.id.et_notes);
        mEtColor.setHint(getString(R.string.prompt_color));
        final EditText mEtColorEditText = mEtColor.getEditText();
        mEtColorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isColorValid(s.toString())) {
                    mEtColor.setErrorEnabled(false);
                    mEtColorEditText.setTextColor(Color.parseColor(mEtColorEditText.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRvColor = v.findViewById(R.id.rv_color);
        mRvColor.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFabConfirm = v.findViewById(R.id.fab_confirm);
        mFabConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAdd();
            }
        });
    }

    private void attemptAdd() {
        mEtTitle.setError(null);
        String title = mEtTitle.getEditText().getText().toString();
        String color = mEtColor.getEditText().getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(title)) {
            mEtTitle.setError(getString(R.string.error_field_required));
            focusView = mEtTitle;
            cancel = true;
        }

        if (TextUtils.isEmpty(color)) {
            mEtColor.setError(getString(R.string.error_field_required));
            focusView = mEtColor;
            cancel = true;
        } else if (!isColorValid(color)) {
            mEtColor.setError(getString(R.string.error_invalid_color));
            focusView = mEtColor;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mCategory.setTitle(title);
            mCategory.setColor(color.toUpperCase());
            getActivity().finish();
        }
    }

    private boolean isColorValid(String color) {
        return Pattern.matches("^#([0-9a-fA-F]{6})$", color);
    }

    private void updateUI() {
        ColorLab colorLab = ColorLab.get(getActivity());
        List<String> colors = colorLab.getColors();
        if (mColorAdapter == null) {
            mColorAdapter = new ColorAdapter(colors);
            mRvColor.setAdapter(mColorAdapter);
        } else {
            mColorAdapter.notifyDataSetChanged();
        }

    }

    private class ColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String mColor;
        private View mVColor;
        private TextView mTvColorCode;

        public ColorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_color, parent, false));
            itemView.setOnClickListener(this);
            mVColor = itemView.findViewById(R.id.v_color);
            mTvColorCode = itemView.findViewById(R.id.tv_color_code);
        }

        public void bind(String color) {
            mColor = color;
            mVColor.getBackground().setTint(Color.parseColor(color));
            mTvColorCode.setText(color);
        }

        @Override
        public void onClick(View v) {
            mEtColor.getEditText().setText(mColor);
        }
    }

    private class ColorAdapter extends RecyclerView.Adapter<ColorHolder> {
        private List<String> mColors;

        public ColorAdapter(List<String> colors) {
            mColors = colors;
        }

        @NonNull
        @Override
        public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ColorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ColorHolder holder, int position) {
            String color = mColors.get(position);
            holder.bind(color);
        }

        @Override
        public int getItemCount() {
            return mColors.size();
        }
    }
}
