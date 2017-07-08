package ru.mobilization.sinjvf.mapsapp.data;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public interface AppSchedulerProvider {
    <T> SingleTransformer<T, T> applyIoSchedulers();

    AppSchedulerProvider DEFAULT = new AppSchedulerProvider() {
        @Override public <T> SingleTransformer<T, T> applyIoSchedulers() {
            return observable -> observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
}