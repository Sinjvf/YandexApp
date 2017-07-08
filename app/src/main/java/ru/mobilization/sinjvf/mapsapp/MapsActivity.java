package ru.mobilization.sinjvf.mapsapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
private final String TAG = "TAG"+getClass().getSimpleName();
    private GoogleMap mMap;

    private BottomSheetBehavior bottomSheetBehavior;

    public static String LATITUDE = "lat";
    public static String LONTITUDE = "lon";
    public static String PLACE_LATLON = "place_latlon";
    public static String PLACE_TITLE = "place_title";
    public static String PLACE_TEXT = "place_text";
    public static String PLACE_PICT = "place_pict";


    @BindView(R.id.bottom_sheet)
    View bottomSheet;

    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.place_pict)
    ImageView imageView;

    public static double DEF_LATITUDE = 55.73367;
    public static double DEF_LONTITUDE = 37.587874;


    private LatLng place;
    private String text;
    private String title;
    private int pictId;



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
    }

    private void getArgs(){
        Intent intent = getIntent();
        place = intent.getParcelableExtra(PLACE_LATLON);
        if (place==null) {
            place = new LatLng(DEF_LATITUDE, DEF_LONTITUDE);
        }
        title = intent.getStringExtra(PLACE_TITLE);
        text = intent.getStringExtra(PLACE_TEXT);
        pictId = intent.getIntExtra(PLACE_PICT, R.drawable.ya);
        title = TextUtils.isEmpty(title)?getString(R.string.title_yandex):title;
        text = TextUtils.isEmpty(text)?getString(R.string.text_yandex):text;
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

        // Add a marker in Sydney and move the camera

        setMarker();
        mMap.setOnMapClickListener(latLng -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));


    }

    private void setMarker(){

        mMap.addMarker(new MarkerOptions().position(place).title(title));
    //    mMap.moveCamera(CameraUpdateFactory.newLatLng(place));

        mMap.setOnMarkerClickListener(marker -> {
            setBottomSheet();
            return true;
        });
        setUpCamera(place, false);
    }

    public void setUpCamera(LatLng place, boolean smooth){
        Log.d(TAG, "setUpCamera: ");
        CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(place, 14);
        CameraUpdate cameraUpdate0 = CameraUpdateFactory.newLatLngZoom(place, 13);

        if (smooth) {
            mMap.animateCamera(cameraUpdate1);
        }else{
            mMap.moveCamera(cameraUpdate0);
            mMap.animateCamera(cameraUpdate1);
        }

    }

    @OnClick(R.id.place_pict)
    public void showPict(){

        ImageView image = (ImageView) getLayoutInflater().inflate(R.layout.dial_picture, null);

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(image);
        Picasso.with(this)
                .load(pictId)
                .into(image);
        dialog.show();
    }

    private void setBottomSheet(){
        textView.setText(text);
        titleView.setText(title);
        Picasso.with(this)
                .load(pictId)
                .into(imageView);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
