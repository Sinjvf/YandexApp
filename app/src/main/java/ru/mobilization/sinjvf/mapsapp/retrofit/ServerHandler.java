package ru.mobilization.sinjvf.mapsapp.retrofit;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import ru.mobilization.sinjvf.mapsapp.R;
import ru.mobilization.sinjvf.mapsapp.retrofit.googlePojo.GoogleDirectionData;

/**
 * Created by asus on 08.07.2017.
 */

public class ServerHandler {

    private ServerContract.MapsAPI service;
    private Context context;

    private static final String googleKey ="AIzaSyCTg8hrU8vU8dY7RunNU5v2lL7qxo07gXY";


    public ServerHandler() {
        service = ServerContract.getService();
    }

    public void getDirection(String origin, String dest,  Callback<GoogleDirectionData> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(ServerContract.P_DEST, dest);
        map.put(ServerContract.P_ORIGIN, origin);
        map.put(ServerContract.P_MODE, "transit");
        map.put(ServerContract.P_KEY, googleKey);
        Call<GoogleDirectionData> call = service.getDirection(map);
        call.enqueue(callback);
    }
}
