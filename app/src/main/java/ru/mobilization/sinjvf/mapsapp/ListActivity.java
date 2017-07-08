package ru.mobilization.sinjvf.mapsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mobilization.sinjvf.mapsapp.Adapter.AdapterItem;
import ru.mobilization.sinjvf.mapsapp.Adapter.CustomAdapter;
import ru.mobilization.sinjvf.mapsapp.Adapter.NavigationItem;
import ru.mobilization.sinjvf.mapsapp.data.local.AppPreferenceManager;
import ru.mobilization.sinjvf.mapsapp.data.local.LocalService;

public class ListActivity extends AppCompatActivity implements IMapNavigation{

    private final String TAG = "ListActivity";
    private AppPreferenceManager preferenceManager;

    @BindView(R.id.listView)
    RecyclerView recyclerView;

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        preferenceManager = new AppPreferenceManager();
        checkFirstTimeLaunch();

        customAdapter = new CustomAdapter(this, getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(customAdapter);

        //TODO: example
        LocalService localService = new LocalService(getApplicationContext());
        localService.getJsonData()
                .subscribe(
                        mobModel -> Log.d(TAG, String.valueOf(mobModel.getPlaces().get(0).getName())),
                        err -> Log.d(TAG, err.getMessage())
                );

        NotificationJob.schedulePeriodicJob();
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

    private void checkFirstTimeLaunch() {
        if (preferenceManager.getFirstTimeUser()) {
            new MaterialDialog.Builder(this)
                    .title(R.string.list_activity_alert_title)
                    .items(R.array.yandex_schools)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            Log.d(TAG, String.valueOf(text.toString()));
                            preferenceManager.setSchool(text.toString());
                            return true;
                        }
                    })
                    .positiveText(R.string.list_activity_ok)
                    .negativeText(R.string.list_activity_skip)
                    .show();
            preferenceManager.setFirstTimeUser(false);
        }
    }
}
