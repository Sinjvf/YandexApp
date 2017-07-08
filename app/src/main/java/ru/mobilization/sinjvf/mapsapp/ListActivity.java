package ru.mobilization.sinjvf.mapsapp;


import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ru.mobilization.sinjvf.mapsapp.Adapter.CustomAdapter;
import ru.mobilization.sinjvf.mapsapp.Adapter.NavigationItem;
import ru.mobilization.sinjvf.mapsapp.data.local.AppPreferenceManager;
import ru.mobilization.sinjvf.mapsapp.data.MapEvent;
import ru.mobilization.sinjvf.mapsapp.data.local.LocalService;
import ru.mobilization.sinjvf.mapsapp.data.model.Location;
import ru.mobilization.sinjvf.mapsapp.data.model.MobModel;
import ru.mobilization.sinjvf.mapsapp.data.model.Place;
import ru.mobilization.sinjvf.mapsapp.data.model.Schedule;

public class ListActivity extends AppCompatActivity implements IMapNavigation{

    private final String TAG = "ListActivity";
    private AppPreferenceManager preferenceManager;

    @BindView(R.id.listView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        preferenceManager = new AppPreferenceManager();
        checkFirstTimeLaunch();

        customAdapter = new CustomAdapter(this, Collections.emptyList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(customAdapter);

        toolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(),android.R.color.white));


    }

    private void filter(List<Schedule> adapterItems, String school) {

        Iterator<Schedule> iterator = adapterItems.iterator();

        while (iterator.hasNext()){
            Schedule item = iterator.next();

            if(item.getDate().getTime()+item.getDuration()*60*60*1000<System.currentTimeMillis()){
                iterator.remove();
            } else {

                boolean show = false;

                for (String sc : item.getSchools()) {

                    if(sc.equals(getEngSchool(school))){
                        show = true;
                        break;
                    }
                }

                if(!show){
                    iterator.remove();
                }

            }

        }
    }

    private String getEngSchool(String school) {
        String res = "";

        switch (school){
            case "ШМР":
                res = "shnr";
                break;
            case "ШРИ":
                res = "shri";
                break;
            case "ШМЯ":
                res = "shm";
                break;
            case "ШМД":
                res = "shmd";
                break;
        }


        return res;
    }

    @Override
    public void navigate(Schedule navigationItem) {
        LocalService localService = new LocalService(getApplicationContext());
        localService.getJsonData()
                .observeOn(Schedulers.newThread())
                .flatMap(new Function<MobModel, SingleSource<MapEvent>>() {
                    @Override
                    public SingleSource<MapEvent> apply(@NonNull MobModel mobModel) throws Exception {

                        MapEvent mapEvent = new MapEvent();

                        mapEvent.setDate(navigationItem.getDate());
                        mapEvent.setName(navigationItem.getLocation());
                        mapEvent.setTitle(navigationItem.getTitle());
                        mapEvent.setLat(Double.parseDouble(getResources().getString(R.string.ya_lat)));
                        mapEvent.setLon(Double.parseDouble(getResources().getString(R.string.ya_lon)));
                        mapEvent.setPictId(getPicture(navigationItem.getLocation()));

                        return Single.just(mapEvent);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(
                mapEvent -> {
                    Intent intent = new Intent(ListActivity.this,MapsActivity.class);
                    intent.putExtra("DATA",mapEvent);
                    startActivity(intent);
                },
                err -> Log.d(TAG, err.getMessage())
        );
    }

    private void checkFirstTimeLaunch() {
        if (preferenceManager.getFirstTimeUser()) {
            new MaterialDialog.Builder(this)
                    .title(R.string.list_activity_alert_title)
                    .items(R.array.yandex_schools)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            Log.d(TAG, String.valueOf(text.toString()));
                            initList(text.toString());
                            preferenceManager.setSchool(text.toString());
                            return true;
                        }
                    })
                    .positiveText(R.string.list_activity_ok)
                    .cancelable(false)
                    .show();
            preferenceManager.setFirstTimeUser(false);
        } else
            initList(preferenceManager.getSchool());
        //Intent intent = new Intent(this,MapsActivity.class);
        //TODO mapping
        //intent.putExtra("DATA",navigationItem);




    }

    private void initList(String school) {

        //TODO: example
        LocalService localService = new LocalService(getApplicationContext());
        localService.getJsonData()
                .subscribe(
                        mobModel -> {
                            List<Schedule> adapterItems = mobModel.getSchedule();
                            filter(adapterItems,school);
                            Collections.sort(adapterItems);
                            customAdapter.setItems(adapterItems);
                            toolbar.setTitle("Расписание (" + preferenceManager.getSchool()+")");
                            customAdapter.notifyItemRangeChanged(0,adapterItems.size());
                        },
                        err -> Log.d(TAG, err.getMessage())
                );

    }

    private int getPicture(String location) {
        int id;
        switch (location){
            case "Мулен руж":
                id = R.drawable.mulen_rouge;
                break;
            case "Синий кит":
                id = R.drawable.blue_whale;
                break;
            case "Экстрополис":
                id = R.drawable.extrapolis;
                break;
            default:
                id = R.drawable.mulen_rouge;
        }

        return id;
    }
}
