
package ru.mobilization.sinjvf.mapsapp.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.mobilization.sinjvf.mapsapp.Adapter.AdapterItem;

public class MobModel {

    @SerializedName("places")
    @Expose
    private List<Place> places = null;
    @SerializedName("schedule")
    @Expose
    private List<Schedule> schedule = null;

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

}
