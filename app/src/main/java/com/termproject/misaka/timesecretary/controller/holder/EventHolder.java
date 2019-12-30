package com.termproject.misaka.timesecretary.controller.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.controller.activity.EventActivity;
import com.termproject.misaka.timesecretary.module.Category;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.utils.TimeUtils;

public class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Event mEvent;
    private View mVDivider;
    private TextView mTvEventStartTime;
    private TextView mTvEventEndTime;
    private TextView mTvEventTitle;
    private TextView mTvEventNotes;
    private CategoryLab mCategoryLab;
    private Context mContext;

    public EventHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.list_item_event, parent, false));
        itemView.setOnClickListener(this);
        mContext = context;
        mCategoryLab = CategoryLab.get(mContext);
        mTvEventStartTime = itemView.findViewById(R.id.tv_event_start_time);
        mTvEventEndTime = itemView.findViewById(R.id.tv_event_end_time);
        mVDivider = itemView.findViewById(R.id.divider);
        mTvEventTitle = itemView.findViewById(R.id.tv_event_title);
        mTvEventNotes = itemView.findViewById(R.id.tv_event_notes);
    }

    public void bind(Event event) {
        mEvent = event;
        Category category = mCategoryLab.getCategory(mEvent.getCategory());
        mTvEventStartTime.setText(TimeUtils.cal2timeString(mEvent.getStartTime()));
        mTvEventEndTime.setText(TimeUtils.cal2timeString(mEvent.getEndTime()));
        mVDivider.setBackgroundColor(Color.parseColor(category.getColor()));
        mTvEventTitle.setText(mEvent.getTitle());
        if (mEvent.getNotes().isEmpty()) {
            mTvEventNotes.setVisibility(View.GONE);
        } else {
            mTvEventNotes.setText(mEvent.getNotes());
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = EventActivity.newIntent(mContext, mEvent.getId());
        mContext.startActivity(intent);
    }
}
