package com.termproject.misaka.timesecretary.controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.module.EventLab;

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2long;

public class NotificationService extends JobService {

    private static final String TAG = "NotificationService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Event e = EventLab.get(this).getUpcomingEvent();
        if (e != null) {
            Intent intent = EventActivity.newIntent(this, e.getId());
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String id = "event";
            String name = "Event";
            String description = "Event Description";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                notificationManager.createNotificationChannel(mChannel);

            }
            Notification notification = new NotificationCompat.Builder(this, id)
                    .setTicker("Ticker")
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentTitle(e.getTitle())
                    .setContentText(e.getNotes())
                    .setSubText("Event")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setWhen(cal2long(e.getStartTime()))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
            notificationManager.notify(0, notification);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}
