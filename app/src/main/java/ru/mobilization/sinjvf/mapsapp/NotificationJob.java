package ru.mobilization.sinjvf.mapsapp;

import android.app.Notification;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

public class NotificationJob extends Job {

    public static final String TAG = "D/Notification";
    private static final int NOTIFY_ID = 101;

    @NonNull
    @Override
    protected Result onRunJob(Params params) { // TODO: В NOTIFICATION НАПИСАТЬ СВОИ ДАННЫЕ!!!
        Notification notification = new NotificationCompat.Builder(getContext())
                .setContentTitle("Напоминание о мобилизации!")
                .setContentText("Скоро лекция!")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(true)
                .setColor(Color.RED)
                .setLocalOnly(true)
                .build();

        NotificationManagerCompat.from(getContext()).notify(NOTIFY_ID, notification);
        return Result.SUCCESS;
    }

    static void schedulePeriodicJob() {
        Log.d(TAG, "schedulePeriodicJob: ");
        int jobId = new JobRequest.Builder(NotificationJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setPersisted(true)
                .build()
                .schedule();
    }
}
