package ru.mobilization.sinjvf.mapsapp.Adapter;

/**
 * Created by Alexander on 05.07.2017.
 */

public class NavigationItem implements AdapterItem {

    private String name;
    private double lat;
    private double lng;

    public NavigationItem(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "NavigationItem{" +
                "name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public NavigationItem(String name){
      this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setName(String name) {
        this.name = name;
    }
}
