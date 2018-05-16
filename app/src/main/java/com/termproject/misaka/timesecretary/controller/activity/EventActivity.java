package com.termproject.misaka.timesecretary.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.termproject.misaka.timesecretary.base.BaseSingleFragmentActivity;
import com.termproject.misaka.timesecretary.controller.fragment.EventFragment;

import java.util.UUID;

/**
 * @author misaka
 */
public class EventActivity extends BaseSingleFragmentActivity {

    private static final String EXTRA_EVENT_ID = "com.termproject.misaka.timesecretary.event_id";

    public static Intent newIntent(Context packageContext, UUID eventId) {
        Intent intent = new Intent(packageContext, EventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        UUID eventId = (UUID) getIntent().getSerializableExtra(EXTRA_EVENT_ID);
        return EventFragment.newInstance(eventId);
    }
}
