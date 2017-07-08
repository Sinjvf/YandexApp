package ru.mobilization.sinjvf.mapsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import ru.mobilization.sinjvf.mapsapp.data.MapEvent;
import ru.mobilization.sinjvf.mapsapp.data.local.LocalService;
import ru.mobilization.sinjvf.mapsapp.data.model.Location;
import ru.mobilization.sinjvf.mapsapp.data.model.MobModel;
import ru.mobilization.sinjvf.mapsapp.data.model.Place;
import ru.mobilization.sinjvf.mapsapp.data.model.Schedule;


public class ListActivity extends AppCompatActivity implements IMapNavigation{

    private final String TAG = "ListActivity";

    @BindView(R.id.listView)
    RecyclerView recyclerView;

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        customAdapter = new CustomAdapter(this, Collections.emptyList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(customAdapter);

        //TODO: example
        LocalService localService = new LocalService(getApplicationContext());
        localService.getJsonData()
                .subscribe(
                        mobModel -> {
                            List<Schedule> adapterItems = mobModel.getSchedule();
                            filter(adapterItems);
                            Collections.sort(adapterItems);
                            customAdapter.setItems(adapterItems);
                            customAdapter.notifyDataSetChanged();
                        },
                        err -> Log.d(TAG, err.getMessage())
                );
    }

    private void filter(List<Schedule> adapterItems) {

        Iterator<Schedule> iterator = adapterItems.iterator();

        while (iterator.hasNext()){
            Schedule item = iterator.next();
            if(item.getDate().getTime()+item.getDuration()*60*60*1000<System.currentTimeMillis()){
                iterator.remove();
            }
        }
    }


    @Override
    public void navigate(Schedule navigationItem) {

        //Intent intent = new Intent(this,MapsActivity.class);
        //TODO mapping
        //intent.putExtra("DATA",navigationItem);

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
