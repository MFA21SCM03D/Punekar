package com.example.punekar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.punekar.Adapters.RecyclerViewAdapter;
import com.example.punekar.Adapters.SlidePageAdapter;
import com.example.punekar.Models.Data;
import com.example.punekar.Models.Slide;
import com.example.punekar.Models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private List<Slide> slidelist;
    private List<Data> mData;
    private ViewPager newspager;
    private TabLayout indicator;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore mRef;
    private CollectionReference usersCollection;
    private User user_info;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isServicesAvailable();



        newspager = findViewById(R.id.News_slider);
        indicator = findViewById(R.id.Slide_tablayout);
        recyclerView = findViewById(R.id.Recycler);
        toolbar = findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);

        user_info = new User();

        mRef = FirebaseFirestore.getInstance();
        usersCollection = mRef.collection("Users");

        slidelist = new ArrayList<>();
        slidelist.add(new Slide(R.drawable.coffee,"Coffee"));
        slidelist.add(new Slide(R.drawable.collab2,"Collab"));
        slidelist.add(new Slide(R.drawable.roommateswanted,"RoomateWanted"));
        slidelist.add(new Slide(R.drawable.thali,"Thali"));

        SlidePageAdapter adapter = new SlidePageAdapter(this, slidelist);
        newspager.setAdapter(adapter);

        indicator.setupWithViewPager(newspager,true);


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MainActivity.Slider_Timer(),4000,6000);


        mData = new ArrayList<>();
        mData.add(new Data("Restaurants", R.drawable.thali,""));
        mData.add(new Data("Hospitals", R.drawable.hospitals2,""));
        mData.add(new Data("Transportation", R.drawable.transportation2,""));
        mData.add(new Data("Payments", R.drawable.payments,""));
        mData.add(new Data("Roommates", R.drawable.roommateswanted,""));
        mData.add(new Data("NGOs", R.drawable.ngo1,""));
        mData.add(new Data("Collaboration", R.drawable.collab2,""));
        mData.add(new Data("Jobs", R.drawable.jobs,""));
        mData.add(new Data("News", R.drawable.news,""));


        RecyclerViewAdapter adapter1 = new RecyclerViewAdapter(this, mData);
        recyclerView.setAdapter(adapter1);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mAuth = FirebaseAuth.getInstance();
        setupFirebaseAuth();


    }

    private void setupFirebaseAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: Signed in");
                    getData(user);


                }
                else{
                    Log.d(TAG, "onAuthStateChanged: Signed out");
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
    }
    private void getData(FirebaseUser user1){
        usersCollection.document(user1.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    user_info = task.getResult().toObject(User.class);
                    Log.d(TAG, "onComplete: " + user_info.getName());

                }
                else{
                    Log.d(TAG, "onComplete: failed to retrieve data");
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
//        Intent intent2 = new Intent(getApplicationContext(), UsersListActivity.class);
//        startActivity(intent2);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.d(TAG, "Before");

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        Log.d(TAG, "After");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
                case R.id.settings:
                Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent1);
                break;
            case R.id.logout:
                mAuth.signOut();
                finish();

        }

        return super.onOptionsItemSelected(item);
    }

    class Slider_Timer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(newspager.getCurrentItem()<slidelist.size() - 1){
                        newspager.setCurrentItem(newspager.getCurrentItem() + 1);
                    }
                    else{
                        newspager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    public boolean isServicesAvailable(){
        Log.d(TAG, "isServicesAvailable: Starting");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesAvailable: Yes");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesAvailable:an error occured ");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,available,ERROR_DIALOG_REQUEST);
            dialog.show();

        }
        else{
            Toast.makeText(this,"Please install google services to use full features of this app",Toast.LENGTH_SHORT).show();
        }
        return false;

    }
}
