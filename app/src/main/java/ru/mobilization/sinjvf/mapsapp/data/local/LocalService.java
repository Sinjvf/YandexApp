package ru.mobilization.sinjvf.mapsapp.data.local;

import android.content.Context;

import com.google.gson.Gson;

import java.io.InputStream;

import io.reactivex.Single;
import ru.mobilization.sinjvf.mapsapp.R;
import ru.mobilization.sinjvf.mapsapp.data.AppSchedulerProvider;
import ru.mobilization.sinjvf.mapsapp.data.model.MobModel;

public class LocalService {

    private Context context;

    public LocalService(Context context) {
        this.context = context;
    }

    public Single<MobModel> getJsonData() {
        return Single.fromCallable(() -> {
            String json;
            InputStream is = context.getResources().openRawResource(R.raw.bot_data);
            int size = is.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            return new Gson().fromJson(json, MobModel.class);
        }).compose(AppSchedulerProvider.DEFAULT.applyIoSchedulers());
    }

}
