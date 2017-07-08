package ru.mobilization.sinjvf.mapsapp.data;

import android.provider.ContactsContract;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by asus on 08.07.2017.
 */

public class MapEvent implements Serializable{



    private double lon;
    private double lat;
    private String name;
    private String title;
    private int pictId;
    private Date date;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPictId() {
        return pictId;
    }

    public void setPictId(int pictId) {
        this.pictId = pictId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MapEvent{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", pictId=" + pictId +
                ", date=" + date +
                '}';
    }
}
