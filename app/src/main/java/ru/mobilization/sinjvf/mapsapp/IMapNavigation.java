package ru.mobilization.sinjvf.mapsapp;

import com.google.android.gms.maps.model.LatLng;

import ru.mobilization.sinjvf.mapsapp.Adapter.NavigationItem;

/**
 * Created by Alexander on 06.07.2017.
 */

public interface IMapNavigation {
    void navigate(NavigationItem navigationItem);
}
