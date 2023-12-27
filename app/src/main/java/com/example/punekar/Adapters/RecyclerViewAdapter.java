package com.example.punekar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.punekar.ComingSoonActivity;
import com.example.punekar.Details;
import com.example.punekar.Models.Data;
import com.example.punekar.R;
import com.example.punekar.RestaurantsActivity;
import com.example.punekar.RoommateWantedActivity;
import com.example.punekar.UsersListActivity;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.mViewHolder> {

    private Context mContext;
    private List<Data> mData;
    
    public RecyclerViewAdapter(Context mContext, List<Data> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflate our cardview in the recyclerview
        View view;
        LayoutInflater minflater = LayoutInflater.from(mContext);
        view = minflater.inflate(R.layout.cardview,parent,false);

        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, final int position) {

        holder.title.setText(mData.get(position).getName());
        holder.img.setImageResource(mData.get(position).getThumbnail());

        //Set onclickListener
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To navigate the user to the respective activity when a particular card is clicked
              if ((mData.get(position).getName()).equals("Collaboration")){
                   Intent intent1 = new Intent(mContext, UsersListActivity.class);
                   mContext.startActivity(intent1);
               }
               else if((mData.get(position).getName()).equals("Restaurants")){
                  Intent intent1 = new Intent(mContext, RestaurantsActivity.class);
                  mContext.startActivity(intent1);

              }
               else if((mData.get(position).getName()).equals("Roommates")){
                  Intent intent1 = new Intent(mContext, RoommateWantedActivity.class);
                  mContext.startActivity(intent1);
              }
               else if ((mData.get(position).getName()).equals("Hospitals")
                        || (mData.get(position).getName()).equals("Transportation")){
                  Intent intent1 = new Intent(mContext, Details.class);
                  mContext.startActivity(intent1);
              }
              else {
                  Intent intent1 = new Intent(mContext, ComingSoonActivity.class);
                  mContext.startActivity(intent1);
              }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class mViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView img;
        CardView cardview;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card1);
            img = itemView.findViewById(R.id.img1);
            cardview = itemView.findViewById(R.id.cardview);
        }
    }
}