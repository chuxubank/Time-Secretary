package com.termproject.misaka.timesecretary.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String CALENDAR_ACCOUNT = "chuxubank@gmail.com";
    private static final String ACCOUNT_TYPE = "com.termproject.misaka.timesecretary";

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    Context mContext;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mContext = context;
    }

    private static Uri asSyncAdapter(Uri uri, String account, String accountType) {
        return uri.buildUpon()
                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(Calendars.ACCOUNT_TYPE, accountType).build();
    }

    /**
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {

//        Uri uri = asSyncAdapter(Calendars.CONTENT_URI, CALENDAR_ACCOUNT, ACCOUNT_TYPE);
//        ContentValues values = new ContentValues();
//        values.put(Calendars.NAME, getContext().getString(R.string.app_name));
//        values.put(Calendars.CALENDAR_DISPLAY_NAME, mContext.getString(R.string.app_name));
//        values.put(Calendars.CALENDAR_COLOR, Color.parseColor("#66CCFF"));
//        values.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
//        values.put(Calendars.OWNER_ACCOUNT, CALENDAR_ACCOUNT);
//        mContentResolver.insert(uri, values);

    }
}
