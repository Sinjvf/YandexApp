package ru.mobilization.sinjvf.mapsapp.data.local;

import com.defaultapps.preferenceshelper.DefaultPreferencesManager;

public class AppPreferenceManager extends DefaultPreferencesManager {

    private final String SCHOOL = "school";

    public String getSchool() {
        return getPreferencesHelper().getString(SCHOOL);
    }

    public void setSchool(String school) {
        getPreferencesHelper().putString(SCHOOL, school);
    }
}
