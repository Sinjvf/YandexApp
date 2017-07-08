package ru.mobilization.sinjvf.mapsapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Alexander on 05.07.2017.
 */

public interface AdapterDelegate<T> {


        /**
         * Called to determine whether this AdapterDelegate is the responsible for the given data
         * element.
         *
         * @param items The data source of the Adapter
         * @param position The position in the datasource
         * @return true, if this item is responsible,  otherwise false
         */
        public boolean isForViewType(@NonNull T items, int position);

        /**
         * Creates the  {@link RecyclerView.ViewHolder} for the given data source item
         *
         * @param parent The ViewGroup parent of the given datasource
         * @return The new instantiated {@link RecyclerView.ViewHolder}
         */
        @NonNull
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);

        /**
         * Called to bind the {@link RecyclerView.ViewHolder} to the item of the datas source set
         *
         * @param items The data source
         * @param position The position in the datasource
         * @param holder The {@link RecyclerView.ViewHolder} to bind
         */
        public void onBindViewHolder(@NonNull T items, int position, @NonNull RecyclerView.ViewHolder holder);

        public int getViewType();
}
