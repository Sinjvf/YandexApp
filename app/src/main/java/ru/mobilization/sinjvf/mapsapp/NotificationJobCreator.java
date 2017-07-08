package ru.mobilization.sinjvf.mapsapp;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class NotificationJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case NotificationJob.TAG:
                return new NotificationJob();
            default:
                return null;
        }
    }
}
