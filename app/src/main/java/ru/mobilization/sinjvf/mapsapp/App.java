package ru.mobilization.sinjvf.mapsapp;

import android.app.Application;
import com.defaultapps.preferenceshelper.PreferencesHelper;
import com.evernote.android.job.JobManager;

import com.defaultapps.preferenceshelper.PreferencesHelper;
import com.evernote.android.job.JobManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        JobManager.create(this).addJobCreator(new NotificationJobCreator());
        JobManager.instance().getConfig().setAllowSmallerIntervalsForMarshmallow(true); // TODO: ЭТО ДЛЯ ТЕСТА, КАК ЗАКОНЧИТЕ СТУКНИТЕ ДАВИТА

        new PreferencesHelper.Builder(this).build();
        new PreferencesHelper.Builder(this)
                .build();

        JobManager.create(this).addJobCreator(new NotificationJobCreator());
        JobManager.instance().getConfig().setAllowSmallerIntervalsForMarshmallow(true); // TODO: ЭТО ДЛЯ ТЕСТА, КАК ЗАКОНЧИТЕ СТУКНИТЕ ДАВИТА

        new PreferencesHelper.Builder(this).build();
    }
}
