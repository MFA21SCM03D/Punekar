package com.example.punekar;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.punekar.Models.Advertise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowDetailsActivity extends AppCompatActivity {

    TextView name, address, contact, roommateNeeded, tenure, totalTenants, rent;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore mRef = FirebaseFirestore.getInstance();
    private CollectionReference roommateCollection = mRef.collection("Advertise");
    FirebaseUser user;
    Advertise advertise_info;

    private static final String TAG = "ShowDetailsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisedetails);

        name = findViewById(R.id.name);
        tenure = findViewById(R.id.tenure);
        contact = findViewById(R.id.contact);
        roommateNeeded = findViewById(R.id.roommate);
        address = findViewById(R.id.address);
        totalTenants = findViewById(R.id.tenants);
        rent = findViewById(R.id.rent);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        setupFirebaseAuth();
    }

    private void setupFirebaseAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();

                if(user != null){

                    Log.d(TAG, "onAuthStateChanged: Signed in");
                    getDetails(user);
                }
                else{
                    Log.d(TAG, "onAuthStateChanged: Signed out");

                }
            }
        };
    }

    private void getDetails(FirebaseUser user) {
        roommateCollection.document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                     advertise_info = task.getResult().toObject(Advertise.class);
                    Log.d(TAG, "onComplete: " + advertise_info.getName());
                    name.setText(advertise_info.getName());
                    roommateNeeded.setText(advertise_info.getRoommateNeeded());
                    address.setText(advertise_info.getAddress());
                    contact.setText(advertise_info.getContact());
                    rent.setText(advertise_info.getRent());
                    tenure.setText(advertise_info.getTenure());
                    totalTenants.setText(advertise_info.getTotalTenants());
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
        super.onBackPressed();
        finish();
    }

}
