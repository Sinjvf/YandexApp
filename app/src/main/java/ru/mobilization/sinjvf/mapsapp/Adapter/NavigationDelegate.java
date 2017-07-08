package ru.mobilization.sinjvf.mapsapp.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mobilization.sinjvf.mapsapp.IMapNavigation;
import ru.mobilization.sinjvf.mapsapp.R;
import ru.mobilization.sinjvf.mapsapp.data.model.Schedule;

/**
 * Created by Alexander on 05.07.2017.
 */

public class NavigationDelegate implements AdapterDelegate<List<? extends AdapterItem>>, OnItemClickListener<Schedule> {

    Integer viewType;
    Toast toast;
    IMapNavigation mapNavigation;

    public NavigationDelegate(Activity activity, Integer type) {
        this.viewType = type;
        this.mapNavigation = (IMapNavigation) activity;
    }




    @Override
    public boolean isForViewType(@NonNull List<? extends AdapterItem> items, int position) {

        return items.get(position) instanceof Schedule;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        toast = Toast.makeText(parent.getContext(),null, Toast.LENGTH_SHORT);
        return new NavigationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_navigation,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<? extends AdapterItem> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        NavigationViewHolder vh = (NavigationViewHolder) holder;
        Schedule testItem = (Schedule) items.get(position);
        vh.event.setText(testItem.getTitle());

        StringBuilder result = new StringBuilder();
        for(int i =0; i < testItem.getTeacher().size(); i++){
            if(i==0){
                result.append(testItem.getTeacher().get(i));
            } else {
                result.append(", ").append(testItem.getTeacher().get(i));
            }
        }

        vh.teacher.setText(result.toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat dateOutputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        Date date1 = new Date();

        try {
            date1 = dateFormat.parse(testItem.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }




        vh.date.setText(dateOutputFormat.format(date1));

        vh.wrapper.setOnClickListener(v -> NavigationDelegate.this.onClick(testItem));

    }

    @Override
    public int getViewType() {
        return viewType;
    }

    @Override
    public void onClick(Schedule item) {
        mapNavigation.navigate(item);
    }

    static class NavigationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_name)
        TextView event;

        @BindView(R.id.teacher_name)
        TextView teacher;

        @BindView(R.id.date_time)
        TextView date;

        @BindView(R.id.wrapper)
        View wrapper;

        public NavigationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
