package com.example.punekar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.punekar.Adapters.RoommateAdvertiseAdapter;
import com.example.punekar.Models.Advertise;
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

public class RoommateWantedActivity extends AppCompatActivity {

    private static final String TAG = "RoommateWantedActivity";
    Toolbar toolbar;
    ImageView back_page, add;
    CardView cardView;
    TextView address;
    List<Advertise> mAdvertise;

    RecyclerView recyclerView;

    StorageReference storageReference;
    Advertise advertise_info;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore mRef = FirebaseFirestore.getInstance();
    private CollectionReference roommateCollection = mRef.collection("Advertise");
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roommatewanted);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");

        back_page = findViewById(R.id.back_page);
        add = findViewById(R.id.add);
        cardView = findViewById(R.id.cardview);
        address = findViewById(R.id.address);
        recyclerView = findViewById(R.id.roommatelist_recycler);

        advertise_info = new Advertise();

        mAdvertise = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setupFirebaseAuth();


        back_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setupFirebaseAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();

                if(user != null){

                    Log.d(TAG, "onAuthStateChanged: Signed in");

                    getAdvertisement(user);
                }
                else{
                    Log.d(TAG, "onAuthStateChanged: Signed out");

                }
            }
        };
    }

    private void getAdvertisement(FirebaseUser user1) {

        roommateCollection.orderBy("name").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mAdvertise.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Advertise advertise = documentSnapshot.toObject(Advertise.class);

                        System.out.println("User Data : \n "+advertise.getName());
                        System.out.println(advertise.getTenure());
                        System.out.println(advertise.getRoommateNeeded());
                        mAdvertise.add(advertise);

                        RoommateAdvertiseAdapter adapter1 = new RoommateAdvertiseAdapter(RoommateWantedActivity.this, mAdvertise);
                        recyclerView.setAdapter(adapter1);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.d(TAG, "Before");

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.roommate_menu,menu);

        Log.d(TAG, "After");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.add:

                Intent intent = new Intent(getApplicationContext(), AddAdvertiseActivity.class);
                startActivity(intent);

                break;
                case R.id.remove:
                roommateCollection.document(user.getUid()).delete();
                finish();
                startActivity(getIntent());
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
