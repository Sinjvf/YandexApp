package ru.mobilization.sinjvf.mapsapp.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Alexander on 05.07.2017.
 */

public class CustomAdapter extends RecyclerView.Adapter {


    final int NAVIGATION_TYPE = 1;


    NavigationDelegate anotherDelegate;

    Activity activity;



    List<AdapterItem> items;

    public CustomAdapter(Activity activity, List<AdapterItem> items) {
        this.items = items;
        this.activity = activity;

        anotherDelegate = new NavigationDelegate(activity, NAVIGATION_TYPE);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType== NAVIGATION_TYPE)
            return anotherDelegate.onCreateViewHolder(parent);

        else throw new IllegalArgumentException();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();

        if(viewType== NAVIGATION_TYPE) {
            anotherDelegate.onBindViewHolder(items, position, holder);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (anotherDelegate.isForViewType(items, position)){
            return anotherDelegate.getViewType();
        }

        throw new IllegalArgumentException("No delegate found");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
