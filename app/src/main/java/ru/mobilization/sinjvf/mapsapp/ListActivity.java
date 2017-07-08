package ru.mobilization.sinjvf.mapsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mobilization.sinjvf.mapsapp.Adapter.AdapterItem;
import ru.mobilization.sinjvf.mapsapp.Adapter.CustomAdapter;
import ru.mobilization.sinjvf.mapsapp.Adapter.NavigationItem;

import static java.security.AccessController.getContext;

public class ListActivity extends AppCompatActivity implements IMapNavigation{


    @BindView(R.id.listView)
    RecyclerView recyclerView;

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        customAdapter = new CustomAdapter(this, getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(customAdapter);

    }


    public List<AdapterItem> getData(){
        ArrayList<AdapterItem> arrayList = new ArrayList<>();

        //TODO элементы

        arrayList.add(new NavigationItem("Test",1,1));

        return arrayList;
        //return Collections.emptyList();
    }


    @Override
    public void navigate(NavigationItem navigationItem) {

    }
}
