package com.example.punekar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.punekar.Models.Advertise;
import com.example.punekar.Models.User;
import com.example.punekar.R;
import com.example.punekar.ShowDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RoommateAdvertiseAdapter extends RecyclerView.Adapter<RoommateAdvertiseAdapter.ViewHolder> {

    private Context mContext;
    private List<Advertise> mAdvertise;
    private FirebaseFirestore mRef = FirebaseFirestore.getInstance();
    private CollectionReference usersCollection = mRef.collection("Users");
    private FirebaseAuth mAuth;

    User user_info;
    Advertise advertise_info;

    public RoommateAdvertiseAdapter(Context mContext, List<Advertise> mAdvertise) {
        this.mContext = mContext;
        this.mAdvertise = mAdvertise;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.roommatewantedlist, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tenure.setText(mAdvertise.get(position).getTenure());
        holder.roommate_needed.setText(mAdvertise.get(position).getRoommateNeeded());


        holder.show_details.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowDetailsActivity.class);
                mContext.startActivity(intent);
            }
        });

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MessageActivity.class);
//                mContext.startActivity(intent);
                Toast.makeText(mContext, "Message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdvertise.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tenure , message, show_details, roommate_needed;
        ImageView user_photo;
        CardView cardview;


        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_photo = itemView.findViewById(R.id.user_photo);
            message = itemView.findViewById(R.id.message);
            show_details = itemView.findViewById(R.id.show_details);
            tenure = itemView.findViewById(R.id.tenure);
            roommate_needed = itemView.findViewById(R.id.roommate_needed);
            cardview = itemView.findViewById(R.id.cardview);

            mAuth = FirebaseAuth.getInstance();
        }
    }

}
