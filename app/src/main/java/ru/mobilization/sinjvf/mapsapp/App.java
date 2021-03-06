package ru.mobilization.sinjvf.mapsapp;

import android.app.Application;

import com.defaultapps.preferenceshelper.PreferencesHelper;
import com.evernote.android.job.JobManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        JobManager.create(this).addJobCreator(new NotificationJobCreator());

        new PreferencesHelper.Builder(this).build();
    }
}
