package com.example.punekar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.punekar.Models.Locations;
import com.example.punekar.R;

import java.util.ArrayList;

public class RecyclerViewLocationsAdapter extends RecyclerView.Adapter<RecyclerViewLocationsAdapter.RecyclerViewHolder>{

    Context mContext;
    ArrayList<Locations> mLocations;

    public RecyclerViewLocationsAdapter(Context mContext, ArrayList<Locations> mLocations) {
        this.mContext = mContext;
        this.mLocations = mLocations;
    }

    @NonNull
    @Override
    public RecyclerViewLocationsAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_places,parent,false);
        RecyclerViewLocationsAdapter.RecyclerViewHolder recyclerViewHolder = new RecyclerViewLocationsAdapter.RecyclerViewHolder(view);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewLocationsAdapter.RecyclerViewHolder holder, int position) {
        String placeName = mLocations.get(position).getPlace_name();

        holder.placeTextView.setText(placeName);

    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView placeTextView;
        RelativeLayout parentLayout;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            placeTextView = itemView.findViewById(R.id.place_name_textView);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }



    }


}
