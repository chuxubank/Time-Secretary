package com.termproject.misaka.timesecretary.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.termproject.misaka.timesecretary.controller.holder.EventHolder;
import com.termproject.misaka.timesecretary.module.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventHolder> {
    private List<Event> mEvents;
    private Context mContext;

    public EventAdapter(List<Event> events, Context context) {
        mEvents = events;
        mContext = context;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new EventHolder(layoutInflater, parent, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public void setEvents(List<Event> events) {
        mEvents = events;
    }
}