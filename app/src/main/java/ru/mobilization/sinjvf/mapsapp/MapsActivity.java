package ru.mobilization.sinjvf.mapsapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mobilization.sinjvf.mapsapp.data.MapEvent;
import ru.mobilization.sinjvf.mapsapp.retrofit.ServerHandler;
import ru.mobilization.sinjvf.mapsapp.retrofit.googlePojo.GoogleDirectionData;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = "TAG" + getClass().getSimpleName();
    private GoogleMap mMap;

    private BottomSheetBehavior bottomSheetBehavior;

    public static String MAP_DATA = "DATA";


    @BindView(R.id.bottom_sheet)
    View bottomSheet;

    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.place_pict)
    ImageView imageView;
    @BindView(R.id.time)
    TextView timeView;
    @BindView(R.id.direct)
    TextView distView;
    @BindView(R.id.go_title)
    TextView goTitle;
    @BindView(R.id.go_container)
    View goContainer;

    public static double DEF_LATITUDE = 55.73367;
    public static double DEF_LONTITUDE = 37.587874;

    private Marker userLocationMarker;
    private Location lastLocation;

    private LatLng place;
    private String text;
    private String title;
    private int pictId;
    private Date date;

    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ButterKnife.bind(this);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        getArgs();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        googleApiClient.connect();

    }



    @Override
    public void onPause() {
        googleApiClient.disconnect();
        super.onPause();
    }


    public void setUpLocation() {
        Log.d(TAG, "setUpLocation: ");
        if (userLocationMarker!=null){
            userLocationMarker.remove();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            Log.d(TAG, "get perm: ");
        }
        sendMyLocale(lastLocation);
    }


    private void sendMyLocale(Location lastLocation){
        if (lastLocation != null) {
            Log.d(TAG, "setUpLocation: not null!");
            LatLng llPosition = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            userLocationMarker = mMap.addMarker(new MarkerOptions()
                    .position(llPosition)
                    .title(getString(R.string.map_you_here))
            );
            //    setUpCamera(llPosition, smooth);


            ServerHandler handler = new ServerHandler();
            handler.getDirection(llPosition.latitude+","+llPosition.longitude, place.latitude+","+place.longitude, new Callback<GoogleDirectionData>() {
                @Override
                public void onResponse(Call<GoogleDirectionData> call, Response<GoogleDirectionData> response) {
                    Log.d(TAG, "onResponse: ");
                    setDist(response.body());
                }

                @Override
                public void onFailure(Call<GoogleDirectionData> call, Throwable t) {
                    Log.d(TAG, "onFailure: ");
                    setDist(null);
                }
            });
        }
    }
    private void getArgs() {

        try {
            Intent intent = getIntent();
            MapEvent data = (MapEvent) intent.getSerializableExtra(MAP_DATA);
            Log.d(TAG, "getArgs: "+data);

            place = new LatLng(data.getLat(), data.getLon());
            title = data.getTitle();
            text = data.getName();
            pictId = data.getPictId();
            date = data.getDate();


        } catch (NullPointerException e) {
            e.printStackTrace();
            place = new LatLng(DEF_LATITUDE, DEF_LONTITUDE);
            title = getString(R.string.title_yandex);
            text = getString(R.string.text_yandex);
            pictId = R.drawable.ya;
            date = Calendar.getInstance().getTime();

        }
    }


    private void setDist(GoogleDirectionData resp) {
        if (resp != null) {
            goTitle.setText(getString(R.string.go));
            goTitle.setVisibility(View.VISIBLE);
            goContainer.setVisibility(View.VISIBLE);
            try {
                String time = resp.getRoutes().get(0).getLegs().get(0).getDuration().getText();
                String dist = resp.getRoutes().get(0).getLegs().get(0).getDistance().getText();
                timeView.setText(time);
                distView.setText(dist);
            }catch(Exception e){
                setDist(null);
            }
        }else{

            goTitle.setText(getString(R.string.fail_location));
            goTitle.setVisibility(View.GONE);
            goContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        setMarker();
        mMap.setOnMapClickListener(latLng -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
    //    mMap.setMyLocationEnabled(true);

    }

    private void setMarker() {

        mMap.addMarker(new MarkerOptions().position(place).title(title));
        //    mMap.moveCamera(CameraUpdateFactory.newLatLng(place));

        mMap.setOnMarkerClickListener(marker -> {
            setBottomSheet();
            setUpLocation();
            return true;
        });
        setUpCamera(place, false);
    }

    public void setUpCamera(LatLng place, boolean smooth) {
        Log.d(TAG, "setUpCamera: ");
        CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(place, 14);
        CameraUpdate cameraUpdate0 = CameraUpdateFactory.newLatLngZoom(place, 13);

        if (smooth) {
            mMap.animateCamera(cameraUpdate1);
        } else {
            mMap.moveCamera(cameraUpdate0);
            mMap.animateCamera(cameraUpdate1);
        }

    }

    @OnClick(R.id.place_pict)
    public void showPict() {

        ImageView image = (ImageView) getLayoutInflater().inflate(R.layout.dial_picture, null);

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(image);
        Picasso.with(this)
                .load(pictId)
                .into(image);
        dialog.show();
    }

    private void setBottomSheet() {
        textView.setText(text);
        titleView.setText(title);
        Picasso.with(this)
                .load(pictId)
                .into(imageView);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Log.d(TAG, "onConnected: ");

        sendMyLocale(lastLocation);
      /*  mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    sendMyLocale(location);
                });*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
    }
}
