package com.example.punekar;


import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.punekar.Adapters.UsersListAdapter;
import com.example.punekar.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {

    private UsersListAdapter usersListAdapter;
     List<User> mUser;

     RecyclerView recyclerView;
     ImageView user_photo;
     TextView username;

    private static final String TAG = "UsersListActivity";

    StorageReference storageReference;
     User user_info;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore mRef = FirebaseFirestore.getInstance();
    private CollectionReference usersCollection = mRef.collection("Users");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userslist);

        recyclerView = findViewById(R.id.chat_list);
        username = findViewById(R.id.user_name);
        user_photo = findViewById(R.id.user_photo);



        user_info = new User();

        mUser = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        setupFirebaseAuth();




    }


    private void setupFirebaseAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();

                if(user != null){

                    Log.d(TAG, "onAuthStateChanged: Signed in");

                    getUsers(user);
                }
                else{
                    Log.d(TAG, "onAuthStateChanged: Signed out");

                }
            }
        };
    }

    private void getUsers(final FirebaseUser user1) {

        usersCollection.orderBy("name").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    mUser.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    User user2 = documentSnapshot.toObject(User.class);

                    if (user1.getUid().equalsIgnoreCase(user2.getUserID())){
                        System.out.println("Comparing ID and printing the Name in UsersListActivity \n" +user1.getUid()+"\n"+user2.getUserID());
                    }
                    else {

                        System.out.println("User Data : \n "+user2.getEmail());
                        System.out.println(user2.getName());
                        System.out.println(user2.getUrl());
                        mUser.add(user2);

                            UsersListAdapter adapter1 = new UsersListAdapter(UsersListActivity.this, mUser);
                            recyclerView.setAdapter(adapter1);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    }
                }



            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
        finish();
    }
}

