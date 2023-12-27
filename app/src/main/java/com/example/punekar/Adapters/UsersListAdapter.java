package com.example.punekar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.punekar.MessageActivity;
import com.example.punekar.Models.User;
import com.example.punekar.R;
import com.example.punekar.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder> {

     private Context mContext;
     private List<User> mUser;
    private FirebaseFirestore mRef = FirebaseFirestore.getInstance();
    private CollectionReference usersCollection = mRef.collection("Users");
    private FirebaseAuth mAuth;
    StorageReference storageReference;
    User user_info;
    private int count = 0, count1 = 0;

    public UsersListAdapter(Context mContext, List<User> mUser) {
        this.mContext = mContext;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.user_list, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.username.setText(mUser.get(position).getName());
        holder.qualification.setText(mUser.get(position).getQualification());


        if (mUser.get(position).getUrl() == null){
            Log.d("UsersListAdapter","Adapter Details : \n"+mUser.get(position).getUrl());
            holder.user_photo.setImageResource(R.drawable.userphoto);

        }
        else{

            user_info = new User();

            storageReference = FirebaseStorage.getInstance().getReference("uploads");
            usersCollection.document(mUser.get(position).getUserID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){

                        user_info = task.getResult().toObject(User.class);

                        StorageReference fileReference = storageReference.child(user_info.getUrl());
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(mContext)
                                        .load(uri).override(360, 360).
                                        diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.user_photo);
                                //Picasso.get().load(mUser.get(position).getUrl()).placeholder(R.drawable.userphoto)
                                //                  .resize(360, 360).centerCrop().into(holder.user_photo);

                            }
                        });
                    }
                }
            });

        }

        holder.profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    ++count;
                    System.out.println("Profile view Count : "+count);
//                    holder.viewCount.setText(""+count);
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("UserID", mUser.get(position).getUserID());
                intent.putExtra("ViewCount",count);
                usersCollection.document(mUser.get(position).getUserID()).update("views",count + user_info.getViews());

                mContext.startActivity(intent);
            }
        });


        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ++count1;
                System.out.println("Collaboration Count : "+count1);

                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("UserID", mUser.get(position).getUserID());
                intent.putExtra("UserName", mUser.get(position).getName());
                intent.putExtra("CollaborationCount",count1);
                usersCollection.document(mUser.get(position).getUserID()).update("collab", count1 + user_info.getCollab());

                mContext.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView username , message, profile, viewCount, qualification;
        ImageView user_photo;
        CardView cardview;


        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_photo = itemView.findViewById(R.id.user_photo);
            message = itemView.findViewById(R.id.message);
            profile = itemView.findViewById(R.id.profile);
            username = itemView.findViewById(R.id.user_name);
            cardview = itemView.findViewById(R.id.cardview);
            viewCount = itemView.findViewById(R.id.views_count);
            qualification = itemView.findViewById(R.id.qualification);


            mAuth = FirebaseAuth.getInstance();
        }
    }

}
